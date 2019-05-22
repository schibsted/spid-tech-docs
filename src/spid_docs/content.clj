(ns spid-docs.content
  "Functions for loading and 'cultivating' (i.e. processing, cross-linking, etc)
   content from the various sources."
  (:require [mapdown.core :as mapdown]
            [spid-docs.cultivate.apis :refer [cultivate-apis]]
            [spid-docs.cultivate.articles :refer [cultivate-articles]]
            [spid-docs.cultivate.endpoints :refer [cultivate-endpoint]]
            [spid-docs.load :refer [load-edn load-edn-str]]
            [stasis.core :as stasis]))

(defn get-types []
  (->> (stasis/slurp-directory "resources/types" #"\.edn$")
       (map (fn [[file content]] (load-edn-str content file)))
       (map (juxt :id identity))
       (into {})))

(defn load-content
  "Loads content from .edn and .md files in resources/. See the readme for a
  run-down of various kinds of contents and where/how they are stored."
  []
  {:endpoints (load-edn "cached-endpoints.edn")
   :articles (mapdown/slurp-directory "resources/articles" #"\.md$")
   :sample-responses (stasis/slurp-directory "generated/sample-responses" #".+\..+$")
   :endpoint-descriptions (mapdown/slurp-directory "resources/endpoints" #"\.md$")
   :filter-descriptions (load-edn "filters.edn")
   :types (get-types)
   :example-params (load-edn "example-params.edn")
   :endpoint-blacklist (load-edn "endpoint-blacklist.edn")})

(defn cultivate-content
  "Combine and cross-link content"
  [raw-content]
  (let [endpoints (mapcat #(cultivate-endpoint % raw-content) (:endpoints raw-content))
        articles (cultivate-articles (:articles raw-content))]
    (-> raw-content
        (assoc
            :endpoints endpoints
            :apis (cultivate-apis endpoints articles)
            :articles articles)
        (dissoc :sample-responses
                :endpoint-descriptions
                :filter-descriptions
                :example-params))))
