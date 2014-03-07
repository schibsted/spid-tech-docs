(ns spid-docs.layout
  (:require [hiccup.page :refer [html5]]
            [optimus.hiccup]
            [optimus.link :as link]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(defn layout-page [request {:keys [title body]}]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title (str title " | SPiD API Documentation")]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])]
   [:body
    [:script "document.body.className = 'js';"]
    [:div#main
     [:div#logo [:a {:href "/"} [:img {:src (link/file-path request "/images/spid.png")}]]]
     [:div#body body]]
    (optimus.hiccup/link-to-js-bundles request ["app.js"])]))
