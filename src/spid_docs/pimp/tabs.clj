(ns spid-docs.pimp.tabs
  "Functions to convert 'tagged' sections to tab containers and tabs.
   A tab group/container is created by a starting a line with :tabs Tabs within
   it can be created by starting a line with :tab ..., where ... is the tab
   title."
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]))

(defn- tease-tabs-out-of-html
  "Finds information about the :tab tags at a given level in a block of html.
   Warning! Regexp ahead."
  [html]
  (as-> html <>
        (str/split <> #"<h2>:tab ")
        (drop 1 <>)
        (map #(str/split % #"</h2>" 2) <>)
        (map (fn [[name contents]] {:name name
                                    :contents contents}) <>)))

(defn- create-tab [{:keys [name contents]}]
  (list
   [:h4 {:class "tab"} name]
   [:div.tab-content contents]))

(defn- create-tabs [tabs]
  (hiccup/html [:div.tabs (map create-tab tabs)]))

(defn transform-tabs
  "Find all occurrences of magic tabs markdown, and replace with the
   corresponding HTML tabs markup."
  [html]
  (str/replace html #"(?sm)<h1>:tabs</h1>(.+?)<h1>:/tabs</h1>"
               (fn [[_ contents]]
                 (-> contents
                     tease-tabs-out-of-html
                     create-tabs))))
