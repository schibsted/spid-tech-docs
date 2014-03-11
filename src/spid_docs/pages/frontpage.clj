(ns spid-docs.pages.frontpage
  (:require [clojure.java.io :as io]
            [spid-docs.cultivate.apis :as a]
            [spid-docs.pages.endpoint-pages :as ep]))

(defn- render-api [api]
  (map #(vector :li [:a {:href (ep/endpoint-url %)}
                     (str "/" (:path %))])
       (:endpoints api)))

(defn- render-service [service]
  (list
   [:h3 (:title service)]
   [:ul (mapcat render-api (:apis service))]))

(defn- render-api-column [num services total-columns]
  [:div {:class (if (= (inc num) total-columns) "lastUnit" "unit s1of3")}
   [:div.item
    (map render-service services)]])

(defn create-page [apis]
  {:title "SPiD API Documentation"
   :body [:div.wrap
          [:h1 "SPiD API Documentation"]
          (slurp (io/resource "frontpage.html"))
          [:div {:class "group" :id "api-reference"}
           [:h2 "API reference"]
           [:p "Looking for API details? Here you will find extensive reference documentation of all API endpoints."]
           [:div.line
            (let [cols 3]
              (map-indexed #(render-api-column %1 %2 cols)
                           (a/columnize apis cols)))]]]})
