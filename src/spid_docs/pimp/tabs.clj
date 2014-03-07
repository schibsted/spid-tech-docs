(ns spid-docs.pimp.tabs
  "Functions to convert 'tagged' headings to tab containers and tabs.
   A tab group/container is created by a heading <h(n)>:tabs ...</h(n)>
   (where n is 1-5). Tabs within it can be created by a heading
   <h(n+1)>:tab ...</h(n+1)>"
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]))

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

(defn transform-tabs
  "Find all occurrences of magic tabs markdown, and replace with the
   corresponding HTML tabs markup."
  [html]
  (str/replace html #"(?s)<h(\d)>:tabs (.+?)</h\1>(.+?)<h\1>:/tabs</h\1>"
               (fn [[_ level header contents]]
                 (str "<h" level ">" header "</h" level ">"
                      (-> contents
                          (tease-tabs-out-of-html header (inc (Integer/parseInt level)))
                          create-tabs)))))
