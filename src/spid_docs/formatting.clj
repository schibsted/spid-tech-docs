(ns spid-docs.formatting
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]
            [me.raynes.cegdown :as md]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn- tease-tabs-out-of-html
  "Finds information about the :tab headers at a given level in a block of html.
   Warning! Regexp ahead."
  [html header level]
  (as-> html <>
        (str/split <> (re-pattern (str "<h" level ">:tab ")))
        (drop 1 <>)
        (map #(str/split % (re-pattern (str "</h" level ">")) 2) <>)
        (map (fn [[name contents]] {:name name
                                    :contents contents
                                    :level level}) <>)))

(defn- create-tab [{:keys [level name contents]}]
  (list
   [(keyword (str "h" level)) {:class "tab"} name]
   [:div.tab-content contents]))

(defn- create-tabs [tabs]
  (hiccup/html [:div.tabs (map create-tab tabs)]))

(defn- transform-tabs
  "Find all occurrences of magic tabs markdown, and replace with the
   corresponding HTML tabs markup."
  [html]
  (str/replace html #"(?s)<h(\d)>:tabs (.+?)</h\1>(.+?)<h\1>:/tabs</h\1>"
               (fn [[_ level header contents]]
                 (str "<h" level ">" header "</h" level ">"
                      (-> contents
                          (tease-tabs-out-of-html header (inc (Integer/parseInt level)))
                          create-tabs)))))

(defn to-html
  "Transforms markdown into HTML."
  [s]
  (if s (md/to-html s pegdown-options) ""))

(defn to-rich-html
  "Transforms markdown into HTML, but also does SPiD-specific transformations."
  [s]
  (-> (to-html s)
      (transform-tabs)))

(defn line-to-html
  "Like to-html, but strips away the paragraph. Useful when you want
   to allow markdown for strong, emphasis or linking - but use the
   contents inline."
  [s]
  (let [html (to-html s)]
    (if (re-find #"<p>" html)
      (subs html 3 (- (count html) 4)) "")))
