(ns spid-docs.frontpage
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.layout :as layout]))

(def cols 3)

(defn- id-to-name [kw]
  (->> (-> (str kw)
           (subs 1)
           (str/split #"-"))
       (map str/capitalize)
       (str/join " ")))

(defn render-api [[title description]]
  (list
   [:dt [:a {:href (str "/apis/" (subs (str title) 1))} (str (id-to-name title) " API")]]
   [:dd description]))

(defn render-service-apis [num service]
  [:div {:class (if (= (inc num) cols) "lastUnit" "unit s1of3")}
   [:div.item
    [:h3 (id-to-name (first service))]
    [:dl.api-listing
     (map render-api (second service))]]])

(defn create-page [apis]
  {:title "SPiD API Documentation"
   :body (list [:h1 "SPiD API Documentation"]
               (slurp (io/resource "frontpage.html"))
               [:div {:class "group api-reference" :id "api-reference"}
                [:h2 "API reference"]
                [:p "Looking for API details? Here you will find extensive reference documentation of all API endpoints."]
                (map #(vector :div.line (map-indexed render-service-apis %))
                     (partition cols cols [] apis))])})
