(ns spid-docs.web
  (:require [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.content :as content]
            [spid-docs.highlight :refer [highlight-code-blocks]]
            [spid-docs.layout :as layout]
            [spid-docs.pages :as pages]
            [stasis.core :as stasis]))

(defn get-assets []
  (assets/load-assets "public" [#"/styles/.*\.css"
                                #"/images/.*\.png"]))

(def optimize optimizations/all)

(defn prepare-page [get-page request]
  (->> (get-page)
       (layout/create-page request)
       (highlight-code-blocks)))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(partial prepare-page %) (vals pages))))

(defn get-pages []
  (let [content (content/load-content)]
    (stasis/merge-page-sources
     {:web-pages (-> (pages/get-pages content)
                     (prepare-pages))
      :exports (pages/get-exports content)})))

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-dir "./dist")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))
