(ns spid-docs.layout
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]))

(defn page [request title body]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title (str title " | SPiD API Documentation")]
    [:link {:rel "stylesheet" :type "text/css" :href (link/file-path request "/styles/spid.css")}]]
   [:body body]))
