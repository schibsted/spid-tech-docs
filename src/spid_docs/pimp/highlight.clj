(ns spid-docs.pimp.highlight
  "Use Pygments to highlight code examples."
  (:require [clygments.core :as pygments]
            [net.cgrand.enlive-html :refer [sniptest html-resource select]]))

(defn- extract-code
  "Pulls out just the highlighted code, removing needless fluff and
   stuff from the Pygments treatment."
  [highlighted]
  (-> highlighted
      java.io.StringReader.
      html-resource
      (select [:pre])
      first
      :content))

(defn highlight
  "Extracts code from the node contents, and highlights it according
   to the given language (extracted from the node's class name)."
  [node]
  (let [code (->> node :content (apply str))
        lang (->> node :attrs :class keyword)]
    {:tag :code
     :content (-> code
                  (pygments/highlight lang :html)
                  (extract-code))}))

(defn highlight-code-blocks
  "Finds all <pre> with <code> in them and highlights with Pygments."
  [markup]
  (sniptest markup
            [:pre :code] highlight
            [:pre] #(assoc-in % [:attrs :class] "codehilite"))) ;; fixme: this matches too many <pre>'s

