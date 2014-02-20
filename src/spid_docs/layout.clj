(ns spid-docs.layout
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]))

(defn create-page [request {:keys [title body]}]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title (str title " | SPiD API Documentation")]
    [:link {:rel "stylesheet" :type "text/css" :href (link/file-path request "/styles/spid.css")}]]
   [:body
    [:div#main
     [:div#logo [:img {:src (link/file-path request "/images/spid.png")}]]
     [:div#body body]]]))
