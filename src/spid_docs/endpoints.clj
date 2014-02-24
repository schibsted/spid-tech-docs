(ns spid-docs.endpoints
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [spid-docs.concepts :refer [concept-path]]
            [spid-docs.content :as content]
            [spid-docs.formatting :refer [to-html line-to-html]]
            [spid-docs.layout :as layout]
            [spid-docs.types :refer [type-path]]))

(defn endpoint-url [endpoint]
  (str "/" (:path endpoint)))

(defn endpoint-path [endpoint]
  (str "/endpoints" (endpoint-url endpoint)))

(defn- render-params-group [[param-def params] param-docs]
  (if param-def
    (list [:dt (str/join ", " params)]
          [:dd [:a {:href (concept-path param-def)} (str "See " (name param-def))]])
    (map #(list [:dt %]
                [:dd (line-to-html (param-docs %))]) params)))

(defn- render-params [heading params param-defs param-docs]
  (if-let [grouped-params (sort-by first (group-by param-defs params))]
    (list [:h3 heading]
          [:dl (map #(render-params-group % param-docs) grouped-params)])))

(defn- render-http-methods [endpoint parameters]
  (let [methods (:httpMethods endpoint)
        url (endpoint-url endpoint)
        param-docs (:parameters endpoint)]
    (mapcat #(list [:h2 (:name %) " " url]
                   (render-params "Required params" (:required %) parameters param-docs)
                   (render-params "Optional params" (:optional %) parameters param-docs)) (vals methods))))

(def format-names {"json" "JSON" "jsonp" "JSON-P"})

(defn- render-key-properties [endpoint]
  [:table.boxed.zebra
   [:tr [:th "Requires authentication"] [:td (if (:auth-required endpoint) "Yes" "No")]]
   (if-let [token-types (:access-token-types endpoint)]
     [:tr
      [:th "Supported access token types"]
      [:td token-types]])
   [:tr
    [:th "Supported response format"]
    [:td (str/join ", " (map format-names (:valid_output_formats endpoint)))]]
   (let [filters (:filters endpoint)]
     [:tr
      [:th (str "Supported filter" (if (= (count filters) 1) "" "s"))]
      [:td (if (seq filters) (str/join ", " filters) "None")]])
   (if-let [filter (:default-filters endpoint)]
     [:tr [:th "Default filters"] [:td filter]])
   [:tr [:th "Successful return"] [:td "200"]]])

(defn- render-field-description [desc]
  (cond
   (nil? desc) ""
   (keyword? desc) (name desc)
   :else (line-to-html desc)))

(defn- render-type [type types]
  (if-let [type-def (first (filter #(= type (:id %)) types))]
    (if (:description type-def)
      [:a {:href (type-path type-def)} (name type)]
      (name type))
    [:a {:href (str "#" (name type))} (name type)]))

(defn- render-type-header [id type-name description]
  (list [:h2 {:id (name id)} type-name]
        (to-html description)))

(defn- render-object [{:keys [id name description fields]} types]
  (list (render-type-header id name description)
        [:table.boxed.zebra
         [:tr [:th "Field"] [:th "Type"] [:th "Description"]]
         (map #(vector :tr
                       [:th (:field %)]
                       [:td (render-type (:type %) types)]
                       [:td (line-to-html (:description %))]) fields)]))

(defn- render-string [{:keys [id name description values]}]
  (list (render-type-header id name description)
        [:table.boxed.zebra
         [:tr [:th "Value"] [:th "Description"]]
         (map #(vector :tr
                       [:th (:value %)]
                       [:td (line-to-html (:description %))]) values)]))

(defn- render-type-defs [endpoint types]
  (map #(if (= (:type %) :object)
          (render-object % types)
          (render-string %)) (:types endpoint)))

(defn render-page [endpoint types parameters]
  {:title (endpoint-url endpoint)
   :body (list [:h1 (:name endpoint)]
               [:p (:description endpoint)]
               (render-key-properties endpoint)
               (render-http-methods endpoint parameters)
               (render-type-defs endpoint types)
               [:pre (with-out-str (pprint endpoint))])})

(defn create-pages [endpoints types parameters]
  (into {} (map (juxt endpoint-path
                      #(partial render-page (content/cultivate-endpoint %) types parameters))
                (:data endpoints))))
