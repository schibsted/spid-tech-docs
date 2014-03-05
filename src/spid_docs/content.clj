(ns spid-docs.content
  (:require [spid-docs.core :as spid]
            [spid-docs.cultivate.apis :as ca]
            [spid-docs.cultivate.endpoints :as ce]
            [stasis.core :as stasis]))

(defn- get-endpoints-from-disk []
  (spid/load-edn "cached-endpoints.edn"))

;(def get-endpoints spid-docs.api/get-endpoints)
(def get-endpoints get-endpoints-from-disk)

(defn load-content []
  (let [endpoints (map ce/cultivate-endpoint (:data (get-endpoints)))]
    {:endpoints endpoints
     :articles (stasis/slurp-directory "resources/articles" #"\.md$")
     :concepts (stasis/slurp-directory "resources/concepts" #"\.md$")
     :params (spid/load-edn "parameters.edn")
     :types (spid/load-edn "types.edn")
     :apis (ca/cultivate-apis (spid/load-edn "apis.edn") endpoints)}))
