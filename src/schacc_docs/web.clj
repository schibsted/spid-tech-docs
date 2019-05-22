(ns spid-docs.web
  "The web namespace defines the basic wiring of the site and post-processing of
   pages for cross-cutting concerns such as injecting optimized asset paths. The
   export function also lives here."
  (:require [clojure.string :as str]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets serve-frozen-assets]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.content :as content]
            [spid-docs.homeless :refer [wrap-utf-8]]
            [spid-docs.pages :as pages]
            [spid-docs.pimp :refer [prepare-pages]]
            [spid-docs.validate-raw :refer [validate-raw-content]]
            [stasis.core :as stasis]))

(defn get-assets
  "Load all scripts, stylesheets and images that we're serving."
  []
  (concat
   (assets/load-assets "public" [#".*"]) ;; make all files in resources/public available on the server
   (assets/load-bundle "public" "app.js" [#"/scripts/lib/.*\.js" ;; gathers all javascript in app.js
                                          #"/scripts/.*\.js"]))) ;; with scripts in lib/ included first

(defn optimize
  "Compress and concatenate CSS and JS as much as possible. Remove individual
   files that have been concatenated, and non-minimized versions of files."
  [assets options]
  (->> (optimizations/all assets options)
       (remove :bundled)
       (remove :outdated)))

(def freeze-pages?
  "Should pages be generated only once, then cached?
   Used by integration tests to speed up the run."
  (= (System/getProperty "spid.freeze.pages") "true"))

(def freeze-assets?
  "Should generated frontend assets be cached in memory?
   Used by integration tests to speed up the run."
  (= (System/getProperty "spid.freeze.assets") "true"))

(defn generate-pages
  "Load all raw contents, check them against the expected schema, transform all
  the raw contents into a nice data structure to work with, generate all pages,
  and finally pimp the pages with code highlighting, tabs, examples, sequence
  diagrams and more."
  []
  (-> (content/load-content)
      validate-raw-content
      content/cultivate-content
      pages/get-pages
      prepare-pages))

(def get-pages
  "Chooses between a caching or non-caching version of generate-pages."
  (if freeze-pages?
    (memoize generate-pages)
    generate-pages))

(def app
  "This is the function we pass to Ring. It will be called with a
   request map for every request."
  (-> get-pages
      (stasis/serve-pages)
      wrap-exceptions
      (optimus/wrap get-assets optimize
                    (if freeze-assets? serve-frozen-assets serve-live-assets))
      wrap-content-type
      wrap-utf-8))

(def export-dir
  "Designate where exported pages should be placed on disk."
  "./dist")

(defn- load-export-dir
  "Load in all files in the export directory.
   Used when diffing changes later."
  []
  (stasis/slurp-directory export-dir #"\.[^.]+$"))

(defn export
  "Export the entire site as flat files to the export-dir."
  []
  (let [assets (optimize (get-assets) {})
        old-files (load-export-dir)]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})
    (println)
    (println "Export complete:")
    (stasis/report-differences old-files (load-export-dir))
    (println)))
