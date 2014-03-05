(ns spid-docs.layout
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]
            [optimus.hiccup]))

(defn create-page [request {:keys [title body]}]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title (str title " | SPiD API Documentation")]
    [:link {:rel "stylesheet" :type "text/css" :href (link/file-path request "/styles/responsive.css")}]]
   [:body
    [:script "document.body.className = 'js';"]
    [:div#main
     [:div#logo [:a {:href "/"} [:img {:src (link/file-path request "/images/spid.png")}]]]
     [:div#body body]]
    (optimus.hiccup/link-to-js-bundles request ["app.js"])]))
