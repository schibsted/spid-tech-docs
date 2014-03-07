(ns spid-docs.pimp
  "PMP, Markup Post Processor, pimps the pages. It renders pages to
   HTML and applies a set of post-processors, such as highlighting
   code examples with Pygments, adding tabs etc."
  (:require [spid-docs.enlive :refer [transform]]
            [spid-docs.homeless :refer [update-vals]]
            [spid-docs.layout :refer [layout-page]]
            [spid-docs.pimp.highlight :refer [highlight-code-blocks]]
            [spid-docs.pimp.tabs :refer [transform-tabs]]))

(defn- add-header-anchors
  "Every h2 gets an id that corresponds to the text inside it. This
   enables users to link to every heading on the whole site."
  [html]
  (transform html [:h2] #(assoc-in % [:attrs :id] (-> % :content first))))

(defn render-page
  "Fetch the page and convert its {:title ... :body ...} map into a
   web page and process the generated markup."
  [get-page request]
  (->> (get-page)
       (layout-page request)
       (highlight-code-blocks)
       (transform-tabs)
       (add-header-anchors)))

(defn prepare-pages
  "Takes a page map, and wraps all its page functions in a call to
   render-page."
  [pages]
  (update-vals pages #(partial render-page %)))
