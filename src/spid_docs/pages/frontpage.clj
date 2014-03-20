(ns spid-docs.pages.frontpage
  (:require [clojure.java.io :as io]
            [spid-docs.formatting :refer [columnize to-id-str]]
            [spid-docs.routes :refer [endpoint-path]]))

(def frontpage-columns 3)

(defn- endpoint-count
  "As we're columnizing a map, each 'api' will be a vector with [key value]. The
  keys in this context are the main API category ('Identity Management', etc).
  The value entry will be the list of apis with individual enpoints. We map over
  the list of APIs by extracting the endpoints and then counting them. By
  reducing over the resulting list of numbers with +, we end up with the total
  number of endpoints in this main API section."
  [api]
  (let [category (first api)
        apis (second api)]
    (reduce + (map (comp count :endpoints) apis))))

(defn- columnize-apis [apis]
  (columnize apis frontpage-columns endpoint-count))

(defn- render-api [api]
  (list
   [:a {:name (to-id-str (:api api))}]
   [:h4 (:api api)]
   [:ul (->> (:endpoints api)
             (map #(vector :li [:a {:href (endpoint-path %)}
                                [:code (name (:method %))] " " (:path %)])))]))

(defn- render-api-section [[api-section apis]]
  (list
   [:a {:name (to-id-str api-section)}]
   [:h3 api-section]
   (mapcat render-api apis)))

(defn- render-api-column [num apis total-columns]
  [:div {:class (if (= (inc num) total-columns) "lastUnit" "unit s1of3")}
   [:div.item (map render-api-section apis)]])

(defn- render-api-columns [apis]
  (let [by-category (group-by (comp first first) apis)]
    (->> (zipmap (keys by-category)
                 (map #(map second %) (vals by-category)))
         columnize-apis
         (map-indexed #(render-api-column %1 %2 frontpage-columns)))))

(defn create-page [apis]
  {:title "SPiD API Documentation"
   :body [:div.wrap
          [:h1 "SPiD API Documentation"]
          (slurp (io/resource "frontpage.html"))
          [:div {:class "group" :id "api-reference"}
           [:h2 "API reference"]
           [:p "Looking for API details? Here you will find extensive reference documentation of all API endpoints."]
           [:div.line (render-api-columns apis)]]]})
