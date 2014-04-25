(ns spid-docs.cultivate.apis
  "Process data in resources/apis.edn into a map of endpoints, where the
  two-level category is key and a map with description and related endpoints
  are the values."
  (:require [spid-docs.homeless :refer [update-vals]]))

(defn- api-tuple [endpoint]
  [(-> endpoint :category :section)
   (-> endpoint :category :api)])

(defn- ->api [endpoints]
  {:endpoints endpoints
   :category (-> endpoints first :category :section)
   :api (-> endpoints first :category :api)})

(defn cultivate-apis
  "Sort the endpoints under their respective API categories."
  [endpoints]
  (update-vals (group-by api-tuple endpoints) ->api))
