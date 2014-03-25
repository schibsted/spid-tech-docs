(ns spid-docs.pages
  "Collects all kinds of pages from various sources"
  (:require [clojure.java.io :as io]
            [spid-docs.pages.article-pages :as articles]
            [spid-docs.pages.endpoint-pages :as endpoints]
            [spid-docs.pages.frontpage :as frontpage]
            [spid-docs.pages.type-pages :as types]
            [stasis.core :as stasis]))

(defn get-pages [content]
  "Returns a map of all pages. The pages are mostly functions that when called
   return page maps. These will eventually be post-processed and turned into HTML
   pages that Stasis will serve/export to disk."
  (stasis/merge-page-sources
   {:general-pages {"/" (partial frontpage/create-page (:apis content))
                    "/endpoints/" (fn [] {:body "TODO - #pagination, #filters, #locale, #formats, #format-json, #format-jsonp, #format-xml"})}
    :endpoints (endpoints/create-pages (:endpoints content) (:types content))
    :articles (articles/create-pages (:articles content))
    :types (types/create-pages (:types content))}))
