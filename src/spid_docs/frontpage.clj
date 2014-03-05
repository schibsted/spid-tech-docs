(ns spid-docs.frontpage
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.apis :as apis]
            [spid-docs.cultivate.apis :as a]
            [spid-docs.endpoints :as ep]
            [spid-docs.layout :as layout]))

(defn render-api [api]
  (map #(vector :li [:a {:href (ep/endpoint-path %)}
                     (str "/" (:path %))])
       (:endpoints api)))

(defn render-service [service]
  (list
   [:h3 (:title service)]
   [:ul (mapcat render-api (:apis service))]))

(defn render-api-column [num services total-columns]
  [:div {:class (if (= (inc num) total-columns) "lastUnit" "unit s1of3")}
   [:div.item
    (map render-service services)]])

(defn create-page [apis]
  {:title "SPiD API Documentation"
   :body (list [:h1 "SPiD API Documentation"]
               (slurp (io/resource "frontpage.html"))
               [:div {:class "group api-reference" :id "api-reference"}
                [:h2 "API reference"]
                [:p "Looking for API details? Here you will find extensive reference documentation of all API endpoints."]
                [:div.line
                 (let [cols 3]
                   (map-indexed #(render-api-column %1 %2 cols)
                                (a/columnize apis cols)))]])})
