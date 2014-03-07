(ns spid-docs.content
  (:require [spid-docs.core :as spid]
            [spid-docs.cultivate.apis :as ca]
            [spid-docs.cultivate.endpoints :as ce]
            [stasis.core :as stasis]))

(defn load-content []
  {:endpoints (:data (spid/load-edn "cached-endpoints.edn"))
   :articles (stasis/slurp-directory "resources/articles" #"\.md$")
   :concepts (stasis/slurp-directory "resources/concepts" #"\.md$")
   :params (spid/load-edn "parameters.edn")
   :types (spid/load-edn "types.edn")
   :apis (spid/load-edn "apis.edn")})

(defn cultivate-content [raw-content]
  (let [endpoints (map ce/cultivate-endpoint (:endpoints raw-content))]
    (assoc raw-content
      :endpoints endpoints
      :apis (ca/cultivate-apis (:apis raw-content) endpoints))))
