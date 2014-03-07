(ns spid-docs.pimp.markdown
  (:require [me.raynes.cegdown :as md]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn parse
  "Transforms markdown into HTML."
  [s]
  (if s (md/to-html s pegdown-options) ""))

(defn inline-parse
  "Like parse, but strips away the paragraph. Useful when you want
   to allow markdown for strong, emphasis or linking - but use the
   contents inline."
  [s]
  (let [html (parse s)]
    (if (re-find #"<p>" html)
      (subs html 3 (- (count html) 4)) "")))
