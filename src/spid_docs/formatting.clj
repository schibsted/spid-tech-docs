(ns spid-docs.formatting
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough :quotes :smarts])

(defn to-html [s]
  (md/to-html s pegdown-options))
