(ns spid-docs.pages.endpoint-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.formatting :refer [to-html line-to-html]]
            [spid-docs.pages.type-pages :refer [type-path]]))

(defn- endpoint-api-path [endpoint]
  "Given an endpoint, return the relative path in the SPiD API."
  (str "/" (:path endpoint)))

(defn endpoint-url [endpoint]
  "Given an endpoint, return the URL to the page in the documentation app."
  (str "/endpoints" (endpoint-api-path endpoint)))

(defn- render-params-group [[param-def params] param-docs]
  (if param-def
    [:tr
     [:th (str/join ", " params)]
     [:td [:a {:href (str "/concepts/" (name param-def) "/")} (str "See " (name param-def))]]]
    (map #(vector :tr
                  [:th %]
                  [:td (line-to-html (param-docs %))]) params)))

(defn- render-params [heading params param-defs param-docs]
  (if-let [grouped-params (sort-by first (group-by param-defs params))]
    (list [:h3 heading]
          [:table.boxed.zebra (map #(render-params-group % param-docs) grouped-params)])))

(defn- render-http-methods [endpoint parameters]
  (let [methods (:httpMethods endpoint)
        url (endpoint-api-path endpoint)
        param-docs (:parameters endpoint {})]
    (mapcat #(list [:h2 (:name %) " " url]
                   (if-let [return-type (get-in endpoint [:http-methods (:name %) :returns])]
                     [:p "Returns " [:a {:href (str "#" (str/capitalize (name return-type)))} return-type]])
                   (render-params "Required params" (:required %) parameters param-docs)
                   (render-params "Optional params" (:optional %) parameters param-docs)) (vals methods))))

(def format-names {"json" "JSON" "jsonp" "JSON-P"})

(defn- render-authentication [auth-required]
  [:tr [:th "Requires authentication"] [:td (if auth-required "Yes" "No")]])

(defn- render-token-type [token-type]
  (if token-type
    [:tr
     [:th "Supported access token type"]
     [:td token-type]]))

(defn- render-output-formats [formats]
  [:tr
   [:th "Supported response format"]
   [:td (str/join ", " (map format-names formats))]])

(defn- render-filters [filters]
  [:tr
   [:th (str "Supported filter" (if (= (count filters) 1) "" "s"))]
   [:td (if (seq filters) (str/join ", " filters) "None")]])

(defn- render-default-filters [default-filters]
  (if default-filters
    [:tr [:th "Default filters"] [:td default-filters]]))

(defn- render-return-status [status]
  [:tr [:th "Successful return"] [:td status]])

(defn- render-key-properties [endpoint]
  [:table.boxed.zebra
   (render-authentication (:auth-required endpoint))
   (render-token-type (:access-token-type endpoint))
   (render-output-formats (:valid_output_formats endpoint))
   (render-filters (:filters endpoint))
   (render-default-filters (:default_filters endpoint))
   (render-return-status (get-in endpoint [:sample-response :status] "200"))])

(defn- render-sample-response [endpoint]
  (if-let [response (get-in endpoint [:sample-response :response])]
    (list [:h2 "Sample response"]
          [:pre [:code.js response]])))

(defn- format-type-name [type]
  (if (keyword? type)
    (name type)
    (str "[" (name (first type)) "...]")))

(defn- render-type [type-id types]
  (let [id (if (keyword? type-id) type-id (first type-id))
        type-def (id types)]
    (if (:description type-def)
      [:a {:href (type-path type-def)} (format-type-name type-id)]
      (format-type-name type-id))))

(defn- render-type-header [id type-name description]
  (list [:h2 {:id (name id)} type-name]
        (to-html description)))

(defmulti render-type-def :type)

(defmethod render-type-def :object [{:keys [id name description fields]} types]
  (list (render-type-header id name description)
        [:table.boxed.zebra
         [:tr [:th "Field"] [:th "Type"] [:th "Description"]]
         (map #(vector :tr
                       [:th (:field %)]
                       [:td (render-type (:type %) types)]
                       [:td (line-to-html (:description %))]) fields)]))

(defmethod render-type-def :object-with-availability [{:keys [id name description fields]} types]
  (list (render-type-header
         id name (str description "\n\nThe availability column indicates if the field always contains a valid non-empty value."))
        [:table.boxed.zebra
         [:tr [:th "Field"] [:th "Type"] [:th "Description"] [:th "Always available"]]
         (map #(vector :tr
                       [:th (:field %)]
                       [:td (render-type (:type %) types)]
                       [:td (line-to-html (:description %))]
                       [:td (if (:always-available %) [:span.check "✓"])]) fields)]))

(defmethod render-type-def :enum [{:keys [id name description values]} _]
  (list (render-type-header id name description)
        [:table.boxed.zebra
         [:tr [:th "Value"] [:th "Description"]]
         (map #(vector :tr
                       [:th (:value %)]
                       [:td (line-to-html (:description %))]) values)]))

(defn- mapify-types [endpoint types]
  (let [mapify (juxt :id identity)]
    (-> {}
        (into (map mapify types))
        (into (map mapify (:types endpoint))))))

(defn- get-pertinent-type-defs [endpoint types]
  (map #(% types) (into #{}
                        (concat (map :returns (-> endpoint :http-methods vals))
                                (map :id (:types endpoint))))))

(defn- redirect-type-links [inline-types]
  (fn [node]
    (let [matches (re-find #"^/types/(.*)" (get-in node [:attrs :href]))
          type-ids (map :id inline-types)
          id (keyword (second matches))]
      (if (and id (some #(= id %) type-ids))
        (assoc-in node [:attrs :href] (str "#" (name id)))
        node))))

(defn- render-pertinent-type-defs [endpoint types]
  (let [pertinent-types (get-pertinent-type-defs endpoint types)
        rendered (map #(render-type-def % types) pertinent-types)]
    (-> (map #(render-type-def % types) pertinent-types)
        (enlive/parse)
        (enlive/transform [:a] (redirect-type-links pertinent-types)))))

(defn create-page [endpoint types parameters]
  {:title (:name endpoint)
   :body (list [:h1 (:name endpoint)]
               (to-html (:description endpoint))
               (render-key-properties endpoint)
               (render-http-methods endpoint parameters)
               (render-sample-response endpoint)
               (render-pertinent-type-defs endpoint (mapify-types endpoint types)))})

(defn create-pages [endpoints types parameters]
  (->> endpoints
       (map (juxt endpoint-url #(partial create-page % types parameters)))
       (into {})))
