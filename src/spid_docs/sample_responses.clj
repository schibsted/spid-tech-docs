(ns spid-docs.sample-responses
  "Tools to generate sample responses for all documentet endpoints. Actually uses
   the API to ensure that sample responses reflect the API, and then runs the
   result through a series of functions that anonymizes and scrambles
   potentially sensitive data."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [spid-docs.api-client :as api]
            [spid-docs.sample-responses.bindings :refer [resolve-bindings]]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-existing eval-in-ns]]
            [spid-docs.sample-responses.wash :refer [wash-data]])
  (:import java.util.Date))

(defn- format-json [data]
  (with-out-str (json/pprint data :escape-slash false)))

(defn format-sample-response [response]
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
                         (assoc :route (:path def))
                         (update-existing [:params] inject-dependencies)
                         (update-existing [:path-params] inject-dependencies))]
    (update-in dep-injected [:path] #(interpolate % (:path-params dep-injected)))))

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
     :response {:status (:status response)
                :error (:error response)
                :data (:data response)
                :success? (:success? response)}}))

(def target-directory "generated/sample-responses")
(def cache-directory "sample-response-cache")

(defn get-file-basename [sample-def]
  (.toLowerCase (str (to-id-str (:route sample-def)) "-" (name (:method sample-def)))))

(defn- get-sample-response-cache-file [sample-def]
  (str cache-directory "/" (get-file-basename sample-def) ".edn"))

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
      (cache-sample-response (fetch-sample-response sample-def))))

(defn- get-endpoint [sample-response endpoints]
  (let [spec [(:method sample-response) (:route sample-response)]]
    (->> endpoints
         (filter #(= spec [(:method %) (:path %)]))
         first)))

(defn- indent [level s]
  (let [indentation (reduce (fn [i _] (str i " ")) "" (range level))]
    (str indentation
         (str/join (str "\n" indentation) (str/split s #"\n")))))

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
  (println (name (:method sample-response)) (:route sample-response))
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
  (let [filename (str target-directory "/" (get-file-basename sample-response) ".json")]
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
          loaded-defs (reduce #(load-def %1 (assoc %2 :access-token-types
                                                   (:access-token-types (get-endpoint %2 endpoints))))
                              [] sample-defs)]
      (doseq [def loaded-defs]
        (generate-files def (get-endpoint def endpoints))))
    (catch clojure.lang.ExceptionInfo e
      (println "-----------------------------------")
      (println "Failed to generate sample responses")
      (println "-----------------------------------")
      (println "Beware! In order for this to work well, you need to add the username and
password for a demo user in resources/config.edn, AND make sure this user
is logged into https://stage.payment.schibsted.no/")
      (println (.getMessage e)))))
