(ns spid-docs.web
  "The web namespace defines the basic wiring of the site and post-processing of
   pages for cross-cutting concerns such as injecting optimized asset paths. The
   export function also lives here."
  (:require [clojure.string :as str]
            [net.cgrand.enlive-html :refer [sniptest]]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [spid-docs.confluence :refer [create-confluence-export create-confluence-export-html to-confluence-url]]
            [spid-docs.content :as content]
            [spid-docs.highlight :refer [highlight-code-blocks]]
            [spid-docs.layout :as layout]
            [spid-docs.pages :as pages]
            [stasis.core :as stasis]))

(defn wrap-content-type-utf-8
  "This function works around the fact that Ring simply chooses the default JVM
  encoding for the response encoding. This is not desirable, we always want to
  send UTF-8."
  [handler]
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
  "Wrap all page functions in a call to prepare-page"
  [pages]
  (zipmap (keys pages)
          (map #(partial prepare-page %) (vals pages))))

(defn export-to-confluence [pages]
  (stasis/merge-page-sources
   {:txt (->> pages
              (map (juxt (partial to-confluence-url "txt") #(partial create-confluence-export pages %)))
              (into {}))
    :html (->> pages
               (map (juxt (partial to-confluence-url "html") #(partial create-confluence-export-html pages %)))
               (into {}))}))

(defn get-pages []
  (let [pages (pages/get-pages (content/load-content))]
    (merge (prepare-pages pages)
           (export-to-confluence pages))))

(def app
  "app is the function we pass to Ring. It will be called with a request map
   for every request."
  (-> (stasis/serve-pages get-pages)
      (optimus/wrap get-assets optimize serve-live-assets)
      wrap-content-type
      wrap-content-type-utf-8))

(def export-dir "./dist")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))
