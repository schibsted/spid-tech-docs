(ns spid-docs.pimp
  "PMP, Markup Post Processor, pimps the pages. It renders pages to
   HTML and applies a set of post-processors, such as highlighting
   code examples with Pygments, adding tabs etc."
  (:require [optimus.link :as link]
            [spid-docs.enlive :refer [transform]]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-vals update-in-existing]]
            [spid-docs.layout :refer [layout-page]]
            [spid-docs.pimp.comments :refer [add-comments]]
            [spid-docs.pimp.examples :refer [inline-examples]]
            [spid-docs.pimp.highlight :refer [highlight-code-blocks-in-page]]
            [spid-docs.pimp.seq-diagrams :refer [insert-svg]]
            [spid-docs.pimp.tabs :refer [transform-tabs]]
            [spid-docs.pimp.toc :refer [create-toc]]))

(defn- get-node-text
  "Given a node, as produced by enlive, find the text, possibly in nested
   nodes."
  [node]
  (let [text (-> node :content first)]
    (if (string? text) text (get-node-text text))))

(defn- wrap-in-anchor
  "Wrap the given content in an enlive element representing an
   anchor, pointing to itself, defined by the provided target id (string)"
  [content target]
  [{:tag :a
    :attrs {:class "anchor-link"
            :id target
            :href (str "#" target)}
    :content content}])

(defn- anchorify-element [element]
  (let [target-id (to-id-str (get-node-text element))]
    (update-in element [:content] #(wrap-in-anchor % target-id))))

(defn- make-headings-clickable
  "Every h2 gets an id that corresponds to the text inside it. This
   enables users to link to every heading on the whole site. The heading
   is also converted into a link so users can copy the URL to a specific
   section with ease."
  [html]
  (transform html [:h2] anchorify-element))

(defn optify
  "Helper that examines paths with the supplied prefix and either subs
  in their cache-busting URLs or returns them unchanged."
  [req prefix]
  (fn [^String src]
    (or (and src
             (.startsWith src prefix)
             (not-empty (link/file-path req src)))
        src)))

(defn optify-images
  "Make sure images are served with cache busters."
  [request html]
  (transform html
             [:img] #(update-in % [:attrs :src] (optify request "/images/"))
             [:a] #(update-in-existing % [:attrs :href] (optify request "/images/"))))

(def skip-pygments?
  (= (System/getProperty "spid.skip.pygments") "true"))

(defn- maybe-highlight-code-blocks [page]
  "Parsing and highlighting with Pygments is quite resource intensive,
   on the order of adding 20 seconds to the full test run. This way we
   can disable the pygments by setting JVM_OPTS=\"-Dspid.skip.pygments=true\""
  (if-not skip-pygments?
    (highlight-code-blocks-in-page page)
    page))

(defn render-page
  "Fetch the page and convert its {:title ... :body ...} map into a
   web page and process the generated markup."
  [get-page request]
  (->> (get-page)
       (layout-page request)
       (inline-examples)
       (transform-tabs)
       (optify-images request)
       (make-headings-clickable)
       (create-toc)
       (add-comments)
       (insert-svg)
       (maybe-highlight-code-blocks)))

(defn prepare-pages
  "Takes a page map, and wraps all its page functions in a call to
   render-page."
  [pages]
  (update-vals pages #(partial render-page %)))
