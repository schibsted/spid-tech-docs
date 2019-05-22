(ns spid-docs.pimp.clickable-headings
  (:require [spid-docs.enlive :refer [transform]]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [get-node-text]]))

(defn- wrap-in-anchor
  "Wrap the given content in an enlive element representing an
   anchor, pointing to itself, defined by the provided target id (string)"
  [content target]
  [{:tag :a
    :attrs {:class "anchor-link"
            :href (str "#" target)}
    :content content}])

(defn- anchorify-element [element]
  (let [target-id (to-id-str (get-node-text element))]
    (-> element
        (update-in [:content] #(wrap-in-anchor % target-id))
        (assoc-in [:attrs :id] target-id))))

(defn make-headings-clickable
  "Every h2 gets an id that corresponds to the text inside it. This
   enables users to link to every heading on the whole site. The heading
   is also converted into a link so users can copy the URL to a specific
   section with ease."
  [html]
  (transform html [:h2] anchorify-element))
