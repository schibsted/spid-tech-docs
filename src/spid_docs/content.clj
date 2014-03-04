(ns spid-docs.content
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.api :as api]
            [spid-docs.core :as spid]
            [stasis.core :as stasis]))

(defn- get-endpoints-from-disk []
  (spid/load-edn "cached-endpoints.edn"))

;(def get-endpoints spid-docs.api/get-endpoints)
(def get-endpoints get-endpoints-from-disk)

(defn endpoint-path-to-filename [path]
  ;; The error endpoint is not really an endpoint, and is the only one
  ;; with an asterisk. Special case it.
  (if (= "api/2/*" path)
    "not-found.edn"
    (-> path
        (str/replace #"[/{}]+" "-")
        (str/replace #"(^-)|(-$)" "")
        (str ".edn"))))

(defn maybe-load-file [file]
  (let [filename (str "resources/" file)]
    (if (->> filename (io/as-file) .exists)
      (spid/load-edn file) {})))

(defn cultivate-endpoint [endpoint]
  (let [endpoint-resource (-> endpoint :path endpoint-path-to-filename)]
    (merge endpoint
           (maybe-load-file (str "endpoints/" endpoint-resource))
           (maybe-load-file (str "sample-responses/" endpoint-resource)))))

(defn load-content []
  {:endpoints (get-endpoints)
   :articles (stasis/slurp-directory "resources/articles" #"\.md$")
   :concepts (stasis/slurp-directory "resources/concepts" #"\.md$")
   :params (spid/load-edn "parameters.edn")
   :types (spid/load-edn "types.edn")
   :apis (spid/load-edn "apis.edn")})
