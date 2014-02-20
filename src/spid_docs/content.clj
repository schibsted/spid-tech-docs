(ns spid-docs.content
  (:require [spid-docs.articles :as articles]
            [spid-docs.endpoints :as endpoints]
            [spid-docs.frontpage :as frontpage]
            [spid-docs.core :as spid]
            [stasis.core :as stasis]))

(defn get-pages []
  (let [endpoints (spid/get-endpoints)
        articles (stasis/slurp-directory "resources/articles" #"\.md$")]
    (stasis/merge-page-sources
     {:general-pages {"/" (partial frontpage/create-page endpoints)}
      :endpoints (endpoints/create-pages endpoints)
      :articles (articles/create-pages articles)})))
