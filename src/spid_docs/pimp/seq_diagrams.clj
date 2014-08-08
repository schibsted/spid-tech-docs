(ns spid-docs.pimp.seq-diagrams
  (:require [clojure.string :as str]
            [spid-docs.seq-diagrams :refer [get-sequence-diagram]]))

(defn- fix [contents]
  (-> contents
      (str/replace "&gt;" ">")))

(def sequence-diagram-re
  #"(?sm)<pre><code class=\"sequence-diagram\">(.+?)</code></pre>")

(defn insert-svg-image [[_ definition]]
  (str "<img src='" (-> definition fix get-sequence-diagram :path) "'>"))

(defn get-sequence-diagrams [html]
  (->> html
       (re-seq sequence-diagram-re)
       (map (fn [[_ definition]] (get-sequence-diagram (fix definition))))))

(defn insert-svg [html]
  {:contents (str/replace html sequence-diagram-re insert-svg-image)
   :dependent-pages (into {} (map (juxt :path :svg) (get-sequence-diagrams html)))})
