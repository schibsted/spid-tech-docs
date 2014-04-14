(ns spid-docs.sample-responses
  "Tools to generate sample responses for all documentet endpoints. Actually uses
   the API to ensure that sample responses reflect the API, and then runs the
   result through a series of functions that anonymizes and scrambles
   potentially sensitive data."
  (:require [clojure.data.json :as json]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [spid-docs.api-client :as api]
            [spid-docs.bindings :refer [resolve-bindings]]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-existing]]
            [spid-docs.sample-responses.wash :refer [wash-data]])
  (:import java.util.Date))

(defn format-sample-response [response]
  (with-out-str (json/pprint (->> response :data wash-data) :escape-slash false)))

(defn- interpolate [string data]
  (str/replace string #"\{(.*)\}" (fn [[_ var]] (str ((keyword var) data)))))

(defn interpolate-sample-def
  "Injects dependencies and interpolates path parameters. Returns an updated
   sample definition map."
  [def & [defs]]
  (let [defs-map (zipmap (map :id defs) (map (comp :data :response) defs))
        inject-dependencies #(eval (resolve-bindings % (:dependencies def) defs-map))
        dep-injected (-> def
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
     :response {:status (:status response)
                :error (:error response)
                :data (:data response)
                :success? (:success? response)}}))

(def target-directory "resources/sample-responses")
(def cache-directory "resources/sample-responses")

(defn get-file-basename [sample-def]
  (.toLowerCase (str (to-id-str (:path sample-def)) "-" (name (:method sample-def)))))

(defn get-cached-sample-response [sample-def]
  (slurp (str cache-directory "/" (get-file-basename sample-def))))

(defn- json-parse-data [response]
  (assoc response :data (:data (json/read-json (:data response)))))

(defn get-sample-response [sample-def]
  (or (get-cached-sample-response sample-def)
      (fetch-sample-response sample-def)))



;; (defn- get-filename-stub [endpoint]
;;   (.toLowerCase (str target-directory "/"
;;                      (to-id-str (:path endpoint)) "-"
;;                      (name (:method endpoint)))))

;; (defn- create-sample-responses [endpoint response]
;;   (let [filename-stub (get-filename-stub endpoint)
;;         sample (process-sample-response response)]
;;     [{:filename (str filename-stub ".json"),  :contents sample}
;;      {:filename (str filename-stub ".jsonp"), :contents (str "callback(" (str/trim sample) ");\n")}]))

;; (defn- ensure-get [endpoint]
;;   (if (not (= (:method endpoint) :GET))
;;     (throw (Exception. (str (name (:method endpoint)) " user sample response not implemented")))))

;; (defn- demo-user-sample [endpoint]
;;   (ensure-get endpoint)
;;   (-> (api/get-config)
;;       (api/get-login-token)
;;       (api/user-get (:path endpoint))
;;       (rename-keys {:body :data
;;                     :status :code})
;;       (json-parse-data)))

;; (defn- save-sample-responses [samples]
;;   (doseq [{:keys [filename contents]} samples]
;;     (spit filename contents)))

;; (defmulti generate-sample-response #(vector (:method %) (:path %)))

;; (defmethod generate-sample-response [:GET "/me"] [endpoint]
;;   (save-sample-responses (create-sample-responses endpoint (demo-user-sample endpoint))))

;; (defmethod generate-sample-response :default [endpoint]
;;   (ensure-get endpoint)
;;   (save-sample-responses (create-sample-responses endpoint (api/GET (:path endpoint)))))
