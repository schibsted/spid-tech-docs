(ns spid-docs.prepare-pages
  (:require [net.cgrand.enlive-html :refer [sniptest]]
            [spid-docs.highlight :refer [highlight-code-blocks]]
            [spid-docs.homeless :refer [update-vals]]
            [spid-docs.layout :as layout]))

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
