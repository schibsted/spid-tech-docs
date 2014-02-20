(ns spid-docs.endpoints
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [spid-docs.content :as content]
            [spid-docs.layout :as layout]))

(defn- render-params [heading params]
  (if (seq params)
    (list [:h3 heading]
          [:ul (map #(vector :li %) params)])))

(defn- render-http-methods [endpoint]
  (let [methods (:httpMethods endpoint)
        url (endpoint-url endpoint)]
    (mapcat #(list [:h2 (:name %) " " url]
                   (render-params "Required params" (:required %))
                   (render-params "Optional params" (:optional %))) (vals methods))))

(defn- format-name [format]
  (cond
   (= "json" format) "JSON"
   (= "jsonp" format) "JSON-P"))

(defn- render-key-properties [endpoint]
  [:table.boxed.zebra
   [:tr [:th "Requires authentication"] [:td (if (:auth-required endpoint) "Yes" "No")]]
   (if-let [token-types (:access-token-types endpoint)]
     [:tr
      [:th "Supported access token types"]
      [:td token-types]])
   [:tr
    [:th "Supported response format"]
    [:td (str/join ", " (map format-name (:valid_output_formats endpoint)))]]
   (let [filters (:filters endpoint)]
     [:tr
      [:th (str "Supported filter" (if (= (count filters) 1) "" "s"))]
      [:td (if (seq filters) (str/join ", " filters) "None")]])
   (if-let [filter (:default-filters endpoint)]
     [:tr [:th "Default filters"] [:td filter]])
   [:tr [:th "Successful return"] [:td "200"]]])

(defn- render-object [type]
  (list [:h2 (:name type)]
        (if-let [description (:description type)] [:p description])
        [:table.boxed.zebra
         [:tr [:th "Field"] [:th "Type"] [:th "Description"]]
         (map #(vector :tr
                       [:th (:field %)]
                       [:td (name (:type %))]
                       [:td (:description %)])
              (:fields type))]))

(defn- render-string [type]
  (list [:h2 {:id (name (:id type))} (:name type)]
        (if-let [description (:description type)] [:p description])
        [:table.boxed.zebra
         [:tr [:th "Value"] [:th "Description"]]
         (map #(vector :tr
                       [:th (:value %)]
                       [:td (:description %)])
              (:values type))]))

(defn- render-types [endpoint]
  (map #(if (= (:type %) :object)
          (render-object %)
          (render-string %)) (:types endpoint)))

(defn endpoint-url [endpoint]
  (str "/" (:path endpoint)))

(defn endpoint-path [endpoint]
  (str "/endpoints" (endpoint-url endpoint)))

(defn render-page [endpoint]
  {:title (endpoint-url endpoint)
   :body (list [:h1 (:name endpoint)]
               [:p (:description endpoint)]
               (render-key-properties endpoint)
               (render-http-methods endpoint)
               (render-types endpoint)
               [:pre (with-out-str (pprint endpoint))])})

(defn create-pages [endpoints]
  (into {} (map (juxt endpoint-path #(partial render-page (content/cultivate-endpoint %)))
                (:data endpoints))))
