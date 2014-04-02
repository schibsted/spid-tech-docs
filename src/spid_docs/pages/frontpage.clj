(ns spid-docs.pages.frontpage
  (:require [clojure.java.io :as io]
            [spid-docs.formatting :refer [columnize to-id-str]]
            [spid-docs.routes :refer [endpoint-path]]))

(def frontpage-columns 3)

(def category-render-order ["Identity Management"
                            "Payment Services"
                            "Authorization"
                            "Data Storage"
                            "Utilities"
                            "Insight"])

(defn- endpoint-count
  "The number of endpoints in an API (e.g. 'Login API') is used to evenly
   distribute APIs across columns."
  [api]
  (-> api :endpoints count))

(defn- columnize-apis
  "Returns a list of lists of apis. Each list represents a column and their
   content is a list of apis to render in the column."
  [apis]
  (columnize apis frontpage-columns endpoint-count))

(defn- render-api
  "Renders a single API: the name and a linked list of endpoints"
  [api]
  (list
   [:h4 {:id (to-id-str (:api api))} (:api api)]
   [:ul (->> (:endpoints api)
             (map #(vector :li [:a {:href (endpoint-path %)}
                                [:code (name (:method %))] " " (:path %)])))]))

(defn- render-api-column [num apis]
  [:div {:class (if (= (inc num) frontpage-columns) "lastUnit" "unit s1of3")}
   (map render-api apis)])

(defn render-service-apis
  "Render the services (e.g. 'Identity management') with all APIs distributed
   across a number of columns (see frontpage-columns towards the top)."
  [category service-apis]
  [:div.line [:h3 {:id (to-id-str category)} category]
   (map-indexed render-api-column (columnize-apis service-apis))])

(defn create-page [apis]
  {:title "SPiD API Documentation"
   :body [:div.wrap
          [:h1 "SPiD API Documentation"]
          (slurp (io/resource "frontpage.html"))
          [:div {:class "group" :id "api-reference"}
           [:h2 "API reference"]
           [:p "Looking for API details? Here you will find extensive reference documentation of all API endpoints."]
           (let [apis-by-category (group-by :category (vals apis))]
             (map #(render-service-apis % (apis-by-category %)) category-render-order))]]})
