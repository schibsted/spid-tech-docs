(ns spid-docs.pages.endpoint-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.example-code :refer [create-example-code]]
            [spid-docs.pages.type-pages :refer [type-path]]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.pimp.markdown :refer [inline-parse]]))

(def lang-names {:curl "cURL" :clojure "Clojure"})
(def lang-classes {:curl "sh" :clojure "clj"})

(defn endpoint-url
  "Given an endpoint, return the URL to the page in the documentation app."
  [endpoint]
  (str "/endpoints/" (:method endpoint) (:path endpoint)))

(defn render-category [endpoint]
  [:a.mod.category.small.faded.mbn
   {:href "#"}
   (get-in endpoint [:category :section])])

(defn render-title [endpoint]
  [:h1.mbn (str (:method endpoint) " " (:path endpoint))])

(defn render-authentication [endpoint]
  (if (:requires-authentication? endpoint)
    [:p.small.faded.mtn
     "Requires authentication with "
     [:strong (str/join " or " (:access-token-types endpoint))]
     " access token."]))

(defn render-description [endpoint]
  [:p "Markdown-beskrivelse **her**"])

(defn render-request-synopsis [endpoint]
  [:pre [:code.sh (str (:method endpoint) " " (:api-path endpoint))]])

(defn- render-request-parameter [parameter]
  [:tr.param
   [:th [:h4.name (:name parameter)]]
   [:td
    [:h5.required (if (= (:type parameter) :path)
                    "required path parameter"
                    (if (:required? parameter) "required" "optional"))]
    [:p.faded.desc (inline-parse (:description parameter))]]])

(defn render-request-parameters [parameters]
  [:table.sectioned
   (map render-request-parameter (->> parameters
                                      (sort-by :type)
                                      (sort-by (comp not :required?))))])

(defn- render-code [lang code]
  [:pre [:code {:class lang} code]])

(defn- render-request-example [[lang example]]
  (list [:h5.tab (lang lang-names)]
        [:div.tab-content
         (if (nil? (:maximal example))
           (render-code (lang lang-classes) (:minimal example))
           (list [:h6.mtm "Minimal example"]
                 (render-code (lang lang-classes) (:minimal example))
                 [:h6.mtm "With all parameters"]
                 (render-code (lang lang-classes) (:maximal example))))]))

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
     (render-request-parameters (:parameters endpoint))]]
   [:div.aside
     [:div.wrap
      (render-request-examples endpoint)]]])

(defn- render-response [endpoint]
  [:div.section
   [:div.main
    [:div.wrap
     [:h1.mbn "Response"]]]])

(defn create-page [endpoint]
  {:split-page? true ;; Makes the layout render a grey right column
   :title (str (:method endpoint) " " (:path endpoint))
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
               (render-response endpoint))})

(defn create-pages [endpoints _ _]
  (->> [{:id "user-id-userId-email-logins"
         :path "/user/{email}/logins"
         :api-path "/api/2/user/{email}/logins"
         :method "GET"
         :name "List logins"
         :category {:section "Identity Management" :api "Login API"}
         :parameters [{:name "ip"
                       :description "Only show login attempts from this IP address"
                       :type :query
                       :required? false}
                      {:name "status"
                       :description "Only show successful (`true`) or failed (`false`) logins"
                       :type :query
                       :required? false}
                      {:name "id/userId/email"
                       :description "Only show successful (`true`) or failed (`false`) logins"
                       :type :path
                       :required? true}]
         :response-formats ["json" "jsonp"]
         :default-response-format "json"
         :pagination ["limit" "since" "offset" "until"]
         :filters ["merchant"]
         :access-token-types ["server"]
         :requires-authentication? true
         :responses {:success {:status 200
                               :description "A list of login attempt objects"
                               :type [:login_attempt]}}}]
       (map (juxt endpoint-url #(partial create-page %)))
       (into {})))
