(ns spid-docs.pages.endpoint-pages
  "Functions to render endpoint pages. All the functions work on readily
   cultivated data from cultivate/endpoints, either the whole map, or parts of
   it. The formal definition of this data structure can be found in
   spid-docs.validate-cultivated"
  (:require [clojure.string :as str]
            [hiccup.core :refer [h]]
            [spid-docs.enlive :as enlive]
            [spid-docs.example-code :refer [create-example-code]]
            [spid-docs.formatting :refer [pluralize enumerate-humanely]]
            [spid-docs.http :refer [get-response-status-name]]
            [spid-docs.pages.type-pages :refer [render-type-definition]]
            [spid-docs.pimp.markdown :refer [render-inline render]]
            [spid-docs.routes :refer [api-path endpoint-path]]))

(def lang-names
  "Human-readable language names"
  {:curl "cURL" :clojure "Clojure" :java "Java" :php "PHP"})

(def lang-classes
  "Classes to use for syntax highlighting, should be a valid Pygments lexer"
  {:curl "sh" :clojure "clj"})

(def format-names
  "Human-readable names for response formats"
  {:json "JSON" :jsonp "JSON-P" :xml "XML"})

(def format-classes
  "Class names representing response formats. Will be used for syntax
   highlighting, and must be avalid Pygments lexer."
  {:json "json" :jsonp "js"})

(def preferred-format-order
  "Sample outputs are rendered in this order"
  [:json :jsonp :xml])

(defn render-category
  "Render a link to the containing API category, e.g. 'Identity Management' etc"
  [endpoint]
  (let [cat (get-in endpoint [:category :section])]
    [:a.mod.category.small.faded.mbn {:href (api-path cat)} cat]))

(defn render-title [endpoint]
  [:h1.mbn (str (name (:method endpoint)) " " (:path endpoint))])

(defn render-authentication
  "Render the kinds of authentication tokens supported by this endpoint."
  [endpoint]
  (if (:requires-authentication? endpoint)
    [:p.small.faded.mtn
     "Requires authentication with "
     [:strong (str/join " or " (map name (:access-token-types endpoint)))]
     " access token."]))

(defn render-description [endpoint]
  (render (:description endpoint)))

(defn render-request-synopsis [endpoint]
  [:pre [:code.sh (str (name (:method endpoint)) " " (:api-path endpoint))]])

(defn- render-request-parameter [parameter]
  [:tr.param
   [:th [:h4.name (:name parameter)]]
   [:td
    [:h5.required (if (= (:type parameter) :path)
                    "required path parameter"
                    (if (:required? parameter) "required" "optional"))]
    [:p.faded.desc (render-inline (:description parameter))]]])

(defn- render-pagination-parameters [parameters]
  [:tr.param {:id "pagination"}
   [:td {:colspan 2}
    [:h4.name
     (enumerate-humanely (map :name parameters))]
    [:p.faded.desc [:a {:href "/endpoints/#pagination"} "Pagination"]]]])

(defn- render-filter-parameter [filters]
  [:tr.param {:id "filter"}
   [:th [:h4.name "filter"]]
   [:td
    [:h5.required "optional"]
    [:dl.faded.desc
     (map #(list [:dt [:code (:name %)]]
                 [:dd (render-inline (:description %))]) filters)]]])

(defn render-request-parameters [parameters & [pagination filters]]
  [:table.sectioned
   (map render-request-parameter (->> parameters
                                      (sort-by :type)
                                      (sort-by (comp not :required?))))
   (if (seq pagination) (render-pagination-parameters pagination))
   (if (seq filters) (render-filter-parameter filters))])

(defn- render-code [lang code]
  [:pre [:code {:class lang} (h code)]])

(defn- render-request-example [[lang example]]
  (let [lang-class (or (lang lang-classes) (name lang))]
    (list [:h5.tab (lang lang-names)]
          [:div.tab-content
           (if (nil? (:maximal example))
             (render-code lang-class (:minimal example))
             (list [:h6.mtm "Minimal example"]
                   (render-code lang-class (:minimal example))
                   [:h6.mtm "With all parameters"]
                   (render-code lang-class (:maximal example))))])))

(defn render-request-examples [endpoint]
  (list
   [:h2 "Example request"]
   [:div.tabs
    (map render-request-example (create-example-code endpoint))]))

(defn render-request [endpoint]
  [:div.section.request
   [:div.main
    [:div.wrap
     [:h1.mbn "Request"]
     (render-request-synopsis endpoint)
     (render-request-parameters (:parameters endpoint) (:pagination endpoint) (:filters endpoint))]]
   [:div.aside
    [:div.wrap
     (render-request-examples endpoint)]]])

(defn- format-response-status [status]
  (str status " " (get-response-status-name status)))

(defn- flag-inline-types
  "Flags types mentioned in the endpoint's inline types as inline, i.e.,
  marks them for inline rendering on the page."
  [endpoint types]
  (reduce (fn [types p] (assoc-in types [p :inline?] true))
          types
          (filter #(contains? types %) (:inline-types endpoint))))

(defn render-response-formats [endpoint]
  [:p
   "This endpoint supports the "
   (->> (:response-formats endpoint)
        (map #(str "<a href=\"/formats/#" (name %) "\">" (% format-names) "</a>"))
        enumerate-humanely)
   (pluralize " response format" (count (:response-formats endpoint)))])

(defn render-response-type
  "Renders the response type along with any supporting inline-types.
   For objects that have nested types (objects or lists of objects), these
   nested structures are typically rendered as inline types. The inline-types
   vector is a list of keywords. The full map of types (flagged as inline or
   not) are required to get the actual type description, and to generate the
   correct links."
  [response inline-types types]
  (list
   [:h2 (str "Success: " (format-response-status (:status response)))]
   (render (:description response))
   (->> inline-types
        (cons (:type response))
        (filter identity) ; The 404 endpoint has no success response
        (map #(if (keyword? %) % (first %)))
        (map (fn [type]
               (if (nil? (type types))
                 (prn (str "Attempted to render undefined type " (name type)))
                 (type types))))
        (filter identity)
        (map #(render-type-definition % types)))))

(defn- render-response-failure [failure]
  [:li
   [:strong (format-response-status (:status failure))]
   " "
   [:span.faded (:description failure)]])

(defn render-response-failures [failures]
  (when (seq failures)
    (list [:h2 "Failure cases"]
          [:ul (map render-response-failure failures)])))

(defn- render-sample-response [format sample]
  (list [:h5.tab (format format-names)]
        [:div.tab-content (render-code (format format-classes) sample)]))

(defn render-sample-responses [samples]
  (list
   [:h2 "Sample response"]
   [:div.tabs
    (map #(render-sample-response % (% samples))
         (sort-by #(.indexOf preferred-format-order %) (keys samples)))]))

(defn- render-response [endpoint types]
  [:div.section
   [:div.main
    [:div.wrap
     [:h1.mbn "Response"]
     (render-response-formats endpoint)
     (render-response-type
      (-> endpoint :responses :success)   ; Success response description
      (:inline-types endpoint)            ; The inline types
      (flag-inline-types endpoint types)) ; The type map with inline types flagged
     (render-response-failures (-> endpoint :responses :failures))]]
   [:div.aside
    [:div.wrap
     (render-sample-responses (-> endpoint :responses :success :samples))]]])

(defn- warn-about-missing-typedefs [endpoint types]
  (if-let [missing (seq (->> (:inline-types endpoint)
                             (filter #(not (contains? types %)))))]
    (binding [*out* *err*]
      (println (str "Endpoint " (name (:method endpoint)) " " (:path endpoint)
                    " references inline type" (if (> (count missing) 1) "s" "")
                    " with missing definition: " (str/join ", " missing))))))

(defn create-page [endpoint types]
  (warn-about-missing-typedefs endpoint types)
  {:split-page? true ;; Makes the layout render a grey right column
   :title (str (name (:method endpoint)) " " (:path endpoint))
   :body (list [:div.section
                [:div.main
                 [:div.wrap
                  (render-category endpoint)
                  (render-title endpoint)
                  (render-authentication endpoint)
                  (render-description endpoint)]]]
               [:div.separator]
               (render-request endpoint)
               [:div.separator]
               (render-response endpoint types))})

(defn create-pages [endpoints types]
  (->> endpoints
       (map (juxt endpoint-path #(partial create-page % types)))
       (into {})))
