(ns spid-docs.web
  "The web namespace defines the basic wiring of the site and post-processing of
   pages for cross-cutting concerns such as injecting optimized asset paths. The
   export function also lives here."
  (:require [net.cgrand.enlive-html :refer [sniptest]]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.content :as content]
            [spid-docs.highlight :refer [highlight-code-blocks]]
            [spid-docs.homeless :refer [wrap-utf-8 update-vals]]
            [spid-docs.layout :as layout]
            [spid-docs.pages :as pages]
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

(defn- add-header-anchors
  "Every h2 gets an id that corresponds to the text inside it. This enables
   users to link to every h2 in the whole site."
  [html]
  (sniptest html [:h2] #(assoc-in % [:attrs :id] (-> % :content first))))

(defn prepare-page
  "Fetch the page and convert its {:title ... :body ...} map into a web page
   and process the generated markup."
  [get-page request]
  (->> (get-page)
       (layout/create-page request)
       (highlight-code-blocks)
       (add-header-anchors)))

(defn prepare-pages
  "Takes a page map, and wraps all its page functions in a call to prepare-page."
  [pages]
  (update-vals pages #(partial prepare-page %)))

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
