(ns spid-docs.sample-responses
  "Tools to generate sample responses for all documented endpoints. Actually uses
   the API to ensure that sample responses reflect the API, and then runs the
   result through a series of functions that anonymizes and scrambles
   potentially sensitive data.

   The actual sample responses are defined in sample_responses/definitions.clj"
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [digest :refer [md5]]
            [spid-docs.api-client :as api]
            [spid-docs.formatting :refer [to-id-str indent]]
            [spid-docs.homeless :refer [update-existing eval-in-ns]]
            [spid-docs.json :refer [format-json]]
            [spid-docs.sample-responses.bindings :refer [resolve-bindings]]
            [spid-docs.sample-responses.wash :refer [wash-data]])
  (:import java.util.Date))

(defn format-sample-response
  "Extract data, pre-process it and return a string of JSON data"
  [response]
  (format-json (->> response :data wash-data)))

(defn- interpolate [string data]
  (str/replace string #"\{(.*?)\}" (fn [[_ var]] (str ((keyword var) data)))))

(defn interpolate-sample-def
  "Injects dependencies and interpolates path parameters. Returns an updated
   sample definition map."
  [def & [defs]]
  (let [defs-map (zipmap (map :id defs) (map (comp :data :response) defs))
        inject-dependencies #(eval-in-ns
                              (resolve-bindings % (:dependencies def) defs-map)
                              (:ns def))
        dep-injected (-> def
                         (update-existing [:params] inject-dependencies)
                         (update-existing [:path-params] inject-dependencies))]
    (assoc dep-injected :path (interpolate (:route dep-injected) (:path-params dep-injected)))))

(defn fetch-sample-response
  "Fetch sample response from the API"
  [sample-def]
  (let [method (:method sample-def)
        path (:path sample-def)
        params (:params sample-def)
        token (if (contains? (:access-token-types sample-def) :server)
                (api/get-server-token)
                (api/get-user-token))
        response (cond
                  (= method :GET) (api/GET token path params)
                  (= method :POST) (api/POST token path params)
                  (= method :DELETE) (api/DELETE token path params))]
    {:id (:id sample-def)
     :method method
     :path (:path sample-def)
     :route (:route sample-def)
     :params (:params sample-def)
     :response {:status (:status response)
                :error (:error response)
                :data (:data response)
                :success? (:success? response)}}))

(def target-directory "generated/sample-responses")
(def cache-directory "sample-response-cache")

(defn get-file-basename [sample-def]
  (.toLowerCase (str (to-id-str (:route sample-def)) "-" (name (:method sample-def)))))

(defn- get-sample-response-cache-file [sample-def]
  (str cache-directory "/"
       (get-file-basename sample-def)
       (when (seq (:params sample-def))
         (str "-" (md5 (str (into (sorted-map) (:params sample-def))))))
       ".edn"))

(defn cache-sample-response
  "Caches successful sample responses and returns the sample response."
  [sample-def]
  (when (<= 200 (-> sample-def :response :status) 299)
    (spit (str "generated/" (get-sample-response-cache-file sample-def))
          (assoc sample-def :cached? true)))
  sample-def)

(defn get-cached-sample-response [sample-def]
  (let [file (io/resource (get-sample-response-cache-file sample-def))]
    (when (not (nil? file))
      (read-string (slurp file)))))

(defn get-sample-response [sample-def]
  (or (get-cached-sample-response sample-def)
      (let [response (fetch-sample-response sample-def)]
        (if (nil? (-> response :response :status))
          (throw (ex-info (with-out-str (pprint response)) {:source :fetch-response}))
          (cache-sample-response response)))))

(defn- get-endpoint [sample-response endpoints]
  (let [spec [(:method sample-response) (:route sample-response)]]
    (->> endpoints
         (filter #(= spec [(:method %) (:path %)]))
         first)))

(defn- verify-sample-response [sample-responses sample-response]
  (let [response (:response sample-response)
        method (name (:method sample-response))
        route (:route sample-response)]
    (when (not (<= 200 (:status response) 299))
      (throw (ex-info (str method " " (:path sample-response)
                           (when (not (= (:path sample-response) route))
                             (str " (" method " " route ")"))
                           "\n    responded with HTTP status " (:status response)
                           "\n" (indent 4 (format-json (:error response))))
                      {:source :generate})))
    (conj sample-responses sample-response)))

(defn- load-and-verify-sample-response [sample-responses def]
  (->> (interpolate-sample-def def sample-responses)
       get-sample-response
       (verify-sample-response sample-responses)))

(defn- force-load-and-verify-sample-response [sample-responses def]
  (->> (interpolate-sample-def def sample-responses)
       fetch-sample-response
       cache-sample-response
       (verify-sample-response sample-responses)))

(defn- write-file [file content]
  (println "    Writing" file)
  (spit file content))

(defn- generate-sample-response-files [sample-response endpoint]
  (println (name (:method sample-response)) (:route sample-response) (:params sample-response))
  (let [formats (:response-formats endpoint)
        json-data (-> sample-response :response format-sample-response)
        expected-status (-> endpoint :responses :success :status)
        actual-status (-> sample-response :response :status)
        output-base (str target-directory "/" (get-file-basename sample-response))]
    (if (:cached? sample-response)
      (println "    (Load from cache)")
      (println "   " (name (:method sample-response)) (:path sample-response) "=>" actual-status))
    (when (not (= expected-status actual-status))
      (throw (ex-info (str "Sample response had unexpected response status.\n"
                           (name (:method sample-response)) " " (:path sample-response)
                           "\nexpected successful response to return " expected-status
                           ", but sample response status was " actual-status) {:source :generate})))
    (when (some #(= % :json) formats)
      (write-file (str output-base ".json") json-data))
    (when (some #(= % :jsonp) formats)
      (write-file (str output-base ".jsonp")
                  (str "callback(" (str/trim json-data) ");\n")))))

(defn- sample-response-missing? [sample-response formats format]
  (let [filename (str target-directory "/" (get-file-basename sample-response) "." (name format))]
    (and (some #(= % format) formats)
         (not (.exists (io/file filename))))))

(defn- generate-missing-sample-response-files [sample-response endpoint]
  (let [formats (:response-formats endpoint)]
    (if (or (sample-response-missing? sample-response formats :json)
            (sample-response-missing? sample-response formats :jsonp))
      (generate-sample-response-files sample-response endpoint)
      (println "Skipping already generated" (name (:method sample-response)) (:path sample-response)))))

(defn generate-sample-responses
  "Generate sample responses in one of three modes:

   :build-missing    Generates missing sample response files. Uses the
                     sample-response-cache wherever possible
   :build-from-cache Regenerates all sample response files, uses the
                     cache wherever possible
   :build-from-api   Regenerates all sample response files, and fetches all
                     responses from the API (and produces new cache files)

Beware! In order for this to work well, you need to add the username and
password for a demo user in resources/config.edn, AND make sure this user
is logged into https://stage.payment.schibsted.no/"
  [sample-defs endpoints mode]
  (if-not (api/config-exists?)
    (do
      (println "Aborting import, no configuration file detected.")
      (println)
      (println "  cp resources/config.sample.edn resources/config.edn")
      (println "  vim resources/config.edn")
      (println))
    (do
      (case mode
        :build-from-cache (println "Re-building generated sample responses from cache")
        :build-from-api (println "Re-fetching all data from the API and re-building generated sample responses")
        (println "Generating missing sample responses.\n\nTo regenerate existing responses, try one of:\n"
                 "   lein generate-sample-responses :build-from-cache\n    lein generate-sample-responses :build-from-api\n"))
      (try
        (let [load-def (if (= :build-from-api mode)
                         force-load-and-verify-sample-response
                         load-and-verify-sample-response)
              generate-files (if (= :build-missing mode)
                               generate-missing-sample-response-files
                               generate-sample-response-files)
              loaded-defs (reduce (fn [defs def]
                                    (if-let [endpoint (get-endpoint def endpoints)]
                                      (load-def defs (assoc def
                                                       :access-token-types
                                                       (:access-token-types endpoint)))
                                      (throw
                                       (ex-info
                                        (str "Unable to find endpoint for route "
                                             (name (:method def))
                                             " "
                                             (:route def)
                                             "\nMake sure path parameters are named correctly")
                                        {:source :load-defs}))))
                                  [] sample-defs)]
          (doseq [def loaded-defs]
            (generate-files def (get-endpoint def endpoints))))
        (catch clojure.lang.ExceptionInfo e
          (println "-----------------------------------")
          (println "Failed to generate sample responses")
          (println "-----------------------------------")
          (println "Beware! In order for this to work well, you need to add the username and
password for a demo user in resources/config.edn, AND make sure this user
is logged into https://stage.payment.schibsted.no/

Original error was
")
          (println (indent 4 (.getMessage e))))))))
