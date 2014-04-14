(ns spid-docs.sample-responses
  "Tools to generate sample responses for all documentet endpoints. Actually uses
   the API to ensure that sample responses reflect the API, and then runs the
   result through a series of functions that anonymizes and scrambles
   potentially sensitive data."
  (:require [clojure.data.json :as json]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [spid-docs.api-client :as api]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-existing]]
            [spid-docs.sample-responses.wash :refer [wash-data]])
  (:import java.util.Date))

(defn format-sample-response [response]
  (with-out-str (json/pprint (->> response :data wash-data) :escape-slash false)))

(defn- interpolate [string data]
  (str/replace string #"\{(.*)\}" (fn [[_ var]] (str ((keyword var) data)))))

(defn inject-deps
  "Takes a map of samples and injects them into the expression. The expression
   can be any Clojure expression. bindings is a map of {Symbol Keyword}. The
   symbols may be used in the expression, and the keywords map to keys in the
   sample defs map.

   Symbols will be replaced with the :response :data from the defs map for
   matching bindings.

   Example:

   (inject-deps {:john {:response {:data {:name \"John\"}}}}
                {'person :john}
                [42 person])
   ;;=>
   [42 \"John\"]

   See tests for further examples"
  [defs bindings expr]
  (let [inject-recursively (partial inject-deps defs bindings)]
    (cond
     ;; When the expression is a symbol, first check if we have a binding for this
     ;; symbol. If we do, check if the binding maps to a sample definition. If it
     ;; does, replace the symbol with the corresponding sample definition's
     ;; :response :data. Otherwise, leave the symbol untouched
     (symbol? expr) (if (and (contains? bindings expr)
                             (contains? defs (bindings expr)))
                      (-> ((bindings expr) defs) :response :data)
                      expr)

     ;; When the expression is a map, recursively inject dependencies in the map
     ;; values. (Keys are left untouched)
     (map? expr) (zipmap (keys expr) (map inject-recursively (vals expr)))

     ;; Seqs and vectors are handled recursively.
     (seq? expr) (map inject-recursively expr)
     (vector? expr) (mapv inject-recursively expr)

     ;; Other expressions are left untouched
     :else expr)))

(defn interpolate-sample-def
  "Injects dependencies and interpolates path parameters. Returns an updated
   sample definition map."
  [def & [defs]]
  (let [defs-map (zipmap (map :id defs) defs)
        inject-dependencies (comp eval (partial inject-deps defs-map (:dependencies def)))
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
