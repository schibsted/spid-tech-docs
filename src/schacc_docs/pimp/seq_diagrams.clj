(ns spid-docs.pimp.seq-diagrams
  (:require [clojure.string :as str]
            [spid-docs.seq-diagrams :refer [get-sequence-diagram]]))

(defn- fix
  "Un-escape the arrowheads in the sequence diagram."
  [contents]
  (-> contents
      (str/replace "&gt;" ">")))

(def sequence-diagram-re
  "A regexp that matches sequence diagrams."
  #"(?sm)<pre><code class=\"sequence-diagram\">(.+?)</code></pre>")

(defn insert-svg-image
  "Replace all sequence diagrams with images linking to the generated svg."
  [[_ definition]]
  (let [svg-path (-> definition fix get-sequence-diagram :path)]
    (str "<a href='" svg-path "'><img src='" svg-path "'></a>")))

(defn get-sequence-diagrams
  "Find all sequence diagram definitions in the HTML string, and generate svg
   out of them."
  [html]
  (->> html
       (re-seq sequence-diagram-re)
       (map (fn [[_ definition]] (get-sequence-diagram (fix definition))))))

(defn insert-svg
  "Takes an HTML string, finds all instances of sequence diagrams in it,
   replaces them with image tags, and returns the HTML contents along with
   the generated svg diagrams as dependent pages."
  [html]
  {:contents (str/replace html
                          sequence-diagram-re insert-svg-image)
   :dependent-pages (into {} (map (juxt :path :svg) (get-sequence-diagrams html)))})
