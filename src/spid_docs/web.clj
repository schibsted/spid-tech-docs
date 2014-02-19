(ns spid-docs.web
  (:require [clojure.pprint :refer [pprint]]
            [hiccup.page :refer [html5]]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.link :as link]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.core :as spid]
            [stasis.core :as stasis]
            [clojure.string :as str]))

(defn page [request title body]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title (str title " | SPiD API Documentation")]
    [:link {:rel "stylesheet" :type "text/css" :href (link/file-path request "/styles/spid.css")}]]
   [:body
    [:h1 "SPiD API endpoints documentation"]
    body]))

(defn get-assets []
  (assets/load-assets "public" [#"/styles/.*\.css"]))

(defn endpoint-url [endpoint]
  (str "/" (:path endpoint)))

(defn endpoint-path [endpoint]
  (str "/endpoints" (endpoint-url endpoint)))

(defn list-enpoints [context endpoints]
  (page
   context
   "Available endpoints"
   [:ul (map #(vector :li (list
                           [:a {:href (endpoint-path %)}
                            (list [:code (:url %)] " - " (:name %))]
                           [:br]
                           (:description %))) (:data endpoints))]))

(defn render-page [endpoint context]
  (page
   context
   "Endpoint"
   (list [:h1 (endpoint-url endpoint)]
         [:p (:description endpoint)]
         [:table
          [:tr [:th "Requires authentication"] [:td "?"]]
          [:tr [:th "Supported access token types"] [:td "?"]]
          [:tr [:th "Supported response format"] [:td (str/join ", " (:valid_output_formats endpoint))]]
          [:tr [:th "Supported filters"] [:td "?"]]
          [:tr [:th "Default filters"] [:td "?"]]
          [:tr [:th "Successful return"] [:td "?"]]]
         [:pre (with-out-str (clojure.pprint/pprint endpoint))])))

(defn get-endpoint-pages [endpoints]
  (into {} (map (juxt endpoint-path
                      #(partial render-page %)) (:data endpoints))))

(defn get-pages [endpoints]
  (merge {"/" #(list-enpoints % endpoints)}
         (get-endpoint-pages endpoints)))

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
