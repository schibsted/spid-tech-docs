(ns spid-docs.content
  "Functions for loading and 'cultivating' (i.e. processing, cross-linking, etc)
   content from the various sources."
  (:require [mapdown.core :as mapdown]
            [spid-docs.core :as spid]
            [spid-docs.cultivate.apis :as ca]
            [spid-docs.cultivate.endpoints :as ce]
            [stasis.core :as stasis]))

(defn get-types []
  (apply merge (spid/load-edn "types.edn")
         (->> (stasis/slurp-directory "resources/types" #"\.edn$")
              (map (fn [[file content]] (spid/load-edn-str content file)))
              (map (juxt :id identity))
              (into {}))))

(defn load-content
  "Loads content from .edn and .md files in resources/. See the readme for a
  run-down of various kinds of contents and where/how they are stored."
  []
  {:endpoints (:data (spid/load-json "cached-endpoints.json"))
   :articles (mapdown/slurp-directory "resources/articles" #"\.md$")
   :concepts (stasis/slurp-directory "resources/concepts" #"\.md$")
   :sample-responses (stasis/slurp-directory "resources/sample-responses" #".+\..+$")
   :endpoint-descriptions (mapdown/slurp-directory "resources/endpoints" #"\.md$")
   :pagination-descriptions (spid/load-edn "pagination.edn")
   :filter-descriptions (spid/load-edn "filters.edn")
   :types (get-types)
   :apis (spid/load-edn "apis.edn")
   :endpoint-blacklist (spid/load-edn "endpoint-blacklist.edn")})

(defn cultivate-content
  "Combine and cross-link content"
  [raw-content]
  (let [endpoints (mapcat #(ce/cultivate-endpoint % raw-content) (:endpoints raw-content))]
    (-> raw-content
        (assoc
            :endpoints endpoints
            :apis (ca/cultivate-apis (:apis raw-content) endpoints))
        (dissoc :sample-responses
                :endpoint-descriptions
                :pagination-descriptions
                :filter-descriptions))))
