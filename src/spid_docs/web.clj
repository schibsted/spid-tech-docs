(ns spid-docs.web
  (:require [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.content :as content]
            [spid-docs.layout :as layout]
            [stasis.core :as stasis]))

(defn get-assets []
  (assets/load-assets "public" [#"/styles/.*\.css"]))

(def optimize optimizations/all)

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(fn [request] (layout/create-page request (%))) (vals pages))))

(defn get-pages []
  (-> (content/get-pages)
      (prepare-pages)))

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-dir "./dist")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (content/get-pages) export-dir {:optimus-assets assets})))
