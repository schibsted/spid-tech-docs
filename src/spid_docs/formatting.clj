(ns spid-docs.formatting
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]
            [me.raynes.cegdown :as md]
            [net.cgrand.enlive-html :as enlive]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn to-html [s]
  (if s (md/to-html s pegdown-options) ""))

(defn line-to-html [s]
  (let [html (to-html s)]
    (if (re-find #"<p>" html)
      (subs html 3 (- (count html) 4)) "")))

(defn parse-html [v]
  (enlive/html-resource (java.io.StringReader. (hiccup/html v))))

(defn select [selector markup]
  (enlive/select (parse-html markup) selector))
