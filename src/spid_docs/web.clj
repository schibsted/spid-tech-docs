(ns spid-docs.web
  (:require [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.articles :as articles]
            [spid-docs.endpoints :as endpoints]
            [spid-docs.core :as spid]
            [spid-docs.layout :as layout]
            [stasis.core :as stasis]))

(defn get-assets []
  (assets/load-assets "public" [#"/styles/.*\.css"]))

(defn list-enpoints [context endpoints]
  (layout/page
   context
   "Available endpoints"
   (list
    [:h1 "SPiD API endpoints documentation"]
    [:ul (map #(vector :li (list
                            [:a {:href (endpoints/endpoint-path %)}
                             (list [:code (:url %)] " - " (:name %))]
                            [:br]
                            (:description %))) (:data endpoints))])))

(defn get-pages [endpoints]
  (merge {"/" #(list-enpoints % endpoints)}
         (endpoints/create-pages endpoints)
         (articles/create-pages)))

(def get-pages-with-endpoints (partial get-pages (spid/get-endpoints)))
(def optimize optimizations/all)

(def app (-> (stasis/serve-pages get-pages-with-endpoints)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-dir "./dist")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages-with-endpoints) export-dir {:optimus-assets assets})))
