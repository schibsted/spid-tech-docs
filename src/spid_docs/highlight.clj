(ns spid-docs.highlight
  (:require [net.cgrand.enlive-html :refer [sniptest html-resource select]]
            [clygments.core :as pygments]))

(defn- extract-code [highlighted]
  (-> highlighted
      java.io.StringReader.
      html-resource
      (select [:pre])
      first
      :content))

(defn highlight [node]
  (let [code (->> node :content (apply str))
        lang (->> node :attrs :class keyword)]
    {:tag :code
     :content (-> code
                  (pygments/highlight lang :html)
                  (extract-code))}))

(defn highlight-code-blocks [markup]
  (sniptest markup
            [:pre :code] highlight
            [:pre] #(assoc-in % [:attrs :class] "codehilite")))
