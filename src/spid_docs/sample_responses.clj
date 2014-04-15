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
            [spid-docs.bindings :refer [resolve-bindings]]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-existing eval-in-ns]]
            [spid-docs.sample-responses.wash :refer [wash-data]])
  (:import java.util.Date))

(defn- format-json [data]
  (with-out-str (json/pprint data :escape-slash false)))

(defn format-sample-response [response]
  (format-json (->> response :data wash-data)))

(defn- interpolate [string data]
  (str/replace string #"\{(.*)\}" (fn [[_ var]] (str ((keyword var) data)))))

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
        response (cond
                  (= method :GET) (api/raw-GET path params)
                  (= method :POST) (api/raw-POST path params)
                  (= method :DELETE) (api/raw-DELETE path params))]
    {:method method
     :path (:path sample-def)
     :route (:route sample-def)
     :response {:status (:status response)
                :error (:error response)
                :data (:data response)
                :success? (:success? response)}}))

(def target-directory "resources/sample-responses")
(def cache-directory "sample-response-cache")

(defn get-file-basename [sample-def]
  (.toLowerCase (str (to-id-str (:route sample-def)) "-" (name (:method sample-def)))))

(defn- get-sample-response-cache-file [sample-def]
  (str cache-directory "/" (get-file-basename sample-def) ".edn"))

(defn cache-sample-response
  "Caches successful sample responses and returns the sample response."
  [sample-def]
  (when (<= 200 (-> sample-def :response :status) 299)
    (spit (str "resources/" (get-sample-response-cache-file sample-def))
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

(defn- load-and-verify-sample-response [sample-responses def]
  (let [interpolated (interpolate-sample-def def sample-responses)
        sample-response (get-sample-response interpolated)
        response (:response sample-response)
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
    (when (:cached? sample-response)
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

(defn generate-sample-responses [sample-defs endpoints]
  (try
    (->> sample-defs
         (reduce load-and-verify-sample-response [])
         (mapv #(generate-sample-response-files % (get-endpoint % endpoints))))
    (catch clojure.lang.ExceptionInfo e
      (println "-----------------------------------")
      (println "Failed to generate sample responses")
      (println "-----------------------------------")
      (println (.getMessage e)))))
