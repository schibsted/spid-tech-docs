(ns spid-docs.sample-responses.generate
  (:require [spid-docs.content :as content]
            [spid-docs.sample-responses.defsample :refer [sample-responses]]
            [spid-docs.sample-responses.definitions]
            [spid-docs.sample-responses :refer [generate-sample-responses]]
            [spid-docs.validate-raw :refer [validate-raw-content]]))

(defn- get-endpoints []
  (-> (content/load-content)
      validate-raw-content
      content/cultivate-content
      :endpoints))

(defn generate []
  (generate-sample-responses @sample-responses (get-endpoints)))
