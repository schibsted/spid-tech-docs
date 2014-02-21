(ns spid-docs.pages
  (:require [spid-docs.articles :as articles]
            [spid-docs.endpoints :as endpoints]
            [spid-docs.frontpage :as frontpage]
            [spid-docs.types :as types]
            [stasis.core :as stasis]))

(defn get-pages [content]
  (stasis/merge-page-sources
   {:general-pages {"/" (partial frontpage/create-page (:endpoints content))}
    :endpoints (endpoints/create-pages (:endpoints content))
    :articles (articles/create-pages (:articles content))
    :types (types/create-pages (:types content))}))

(defn get-exports [content]
  (articles/create-confluence-exports (:articles content)))
