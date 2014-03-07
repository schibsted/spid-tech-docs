(ns spid-docs.web
  "The web namespace defines the basic wiring of the site and post-processing of
   pages for cross-cutting concerns such as injecting optimized asset paths. The
   export function also lives here."
  (:require [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.content :as content]
            [spid-docs.homeless :refer [wrap-utf-8]]
            [spid-docs.pages :as pages]
            [spid-docs.prepare-pages :refer [prepare-pages]]
            [spid-docs.validate :refer [validate-raw-content]]
            [stasis.core :as stasis]))

(defn get-assets
  "List all scripts, stylesheets and images that we're serving."
  []
  (concat
   (assets/load-bundle "public" "app.js" [#"/scripts/lib/.*\.js"
                                          #"/scripts/.*\.js"])
   (assets/load-assets "public" [#".*"])))

(def optimize
  "Compress and concatenate CSS and JS as much as possible"
  optimizations/all)

(defn get-pages []
  (-> (content/load-content)
      validate-raw-content
      content/cultivate-content
      pages/get-pages
      prepare-pages))

(def app
  "This is the function we pass to Ring. It will be called with a
   request map for every request."
  (-> (stasis/serve-pages get-pages)
      (optimus/wrap get-assets optimize serve-live-assets)
      wrap-content-type
      wrap-utf-8))

(def export-dir "./dist")

(defn export
  "Export the entire site as flat files to the export-dir."
  []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))
