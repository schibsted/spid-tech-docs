(ns spid-docs.web
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]
            [net.cgrand.enlive-html :refer [sniptest]]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.confluence :as confluence]
            [spid-docs.content :as content]
            [spid-docs.highlight :refer [highlight-code-blocks]]
            [spid-docs.layout :as layout]
            [spid-docs.pages :as pages]
            [stasis.core :as stasis]))

(defn wrap-content-type-utf-8 [handler]
  (fn [request]
    (when-let [response (handler request)]
      (if (.contains (get-in response [:headers "Content-Type"]) ";")
        response
        (if (string? (:body response))
          (update-in response [:headers "Content-Type"] #(str % "; charset=utf-8"))
          response)))))

(defn get-assets []
  (concat
   (assets/load-bundle "public" "app.js" [#"/scripts/lib/.*\.js"
                                          #"/scripts/.*\.js"])
   (assets/load-assets "public" [#"/styles/.*\.css"
                                 #"/images/.*\.png"])))

(def optimize optimizations/all)

(defn to-confluence-url [[url _]]
  (if (= url "/")
    "/index.csf.txt"
    (-> url
        (str/replace #"/$" "")
        (str ".csf.txt"))))

(defn create-confluence-export [[_ get-page] _]
  (-> (get-page) :body hiccup/html confluence/to-storage-format))

(defn export-to-confluence [pages]
  (->> pages
       (map (juxt to-confluence-url #(partial create-confluence-export %)))
       (into {})))

(defn- add-header-anchors [html]
  (sniptest html [:h2] #(assoc-in % [:attrs :id] (-> % :content first))))

(defn prepare-page [get-page request]
  (->> (get-page)
       (layout/create-page request)
       (highlight-code-blocks)
       (add-header-anchors)))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(partial prepare-page %) (vals pages))))

(defn get-pages []
  (let [pages (pages/get-pages (content/load-content))]
    (merge (prepare-pages pages)
           (export-to-confluence pages))))

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type
             wrap-content-type-utf-8))

(def export-dir "./dist")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))
