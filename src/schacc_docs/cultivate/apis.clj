(ns spid-docs.cultivate.apis
  "Process data in resources/apis.edn into a map of endpoints, where the
  two-level category is key and a map with description and related endpoints
  are the values."
  (:require [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-vals in?]]
            [spid-docs.routes :refer [article-path]]))

(defn- api-tuple [endpoint]
  [(-> endpoint :category :section)
   (-> endpoint :category :api)])

(defn- ->api [endpoints articles]
  (let [api-name (-> endpoints first :category :api)
        article (str "/" (to-id-str api-name) ".md")
        api {:endpoints endpoints
             :category (-> endpoints first :category :section)
             :api api-name}]
    (if (in? (vec (keys articles)) article)
      (assoc api :url (article-path article))
      api)))

(defn cultivate-apis
  "Sort the endpoints under their respective API categories."
  [endpoints articles]
  (update-vals (group-by api-tuple endpoints) #(->api % articles)))
