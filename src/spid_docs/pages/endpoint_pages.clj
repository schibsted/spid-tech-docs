(ns spid-docs.pages.endpoint-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.example-code :refer [create-example-code]]
            [spid-docs.http :refer [get-response-status-name]]
            [spid-docs.pages.type-pages :refer [type-path]]
            [spid-docs.pimp.markdown :refer [render-inline render]]))

(def lang-names {:curl "cURL" :clojure "Clojure"})
(def lang-classes {:curl "sh" :clojure "clj"})
(def format-names {:json "JSON" :jsonp "JSON-P" :xml "XML"})
(def format-classes {:json "js" :jsonp "js"})
(def preferred-format-order [:json :jsonp :xml])

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
  (render (:description endpoint)))

(defn render-request-synopsis [endpoint]
  [:pre [:code.sh (str (:method endpoint) " " (:api-path endpoint))]])

(defn- render-request-parameter [parameter]
  [:tr.param
   [:th [:h4.name (:name parameter)]]
   [:td
    [:h5.required (if (= (:type parameter) :path)
                    "required path parameter"
                    (if (:required? parameter) "required" "optional"))]
    [:p.faded.desc (render-inline (:description parameter))]]])

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

(defn- enumerate-humanely
  "Take a list of words, and return a formatted string that enumerates the words
   in a human way, e.g. 'cow, horse and pig'."
  [coll]
  (str/join "" (drop
                1 (interleave
                   (into
                    (list (str (if (= (count coll) 2) " and " ", and ")) "")
                    (repeat (dec (count coll)) ", "))
                   coll))))

(defn- pluralize [word count]
  (if (= count 1) word (str word "s")))

(defn- format-response-status [status]
  (str status " " (get-response-status-name status)))

(defn render-response-formats [endpoint]
  [:p
   "This endpoint supports the "
   (enumerate-humanely (:response-formats endpoint))
   (pluralize " response format" (count (:response-formats endpoint)))])

(defn render-response-type [response]
  (list
   [:h2 (str "Success: " (format-response-status (:status response)))]
   (render (:description response))))

(defn- render-response-failure [failure]
  [:li
   [:strong (format-response-status (:status failure))]
   " "
   [:span.faded (:description failure)]])

(defn render-response-failures [failures]
  (list [:h2 "Failure cases"]
        [:ul (map render-response-failure failures)]))

(defn- render-sample-response [format sample]
  (list [:h5.tab (format format-names)]
        [:div.tab-content (render-code (format format-classes) sample)]))

(defn render-sample-responses [samples]
  (list
   [:h2 "Sample response"]
   [:div.tabs
    (map #(render-sample-response % (% samples))
         (sort-by #(.indexOf preferred-format-order %) (keys samples)))]))

(defn- render-response [endpoint]
  [:div.section
   [:div.main
    [:div.wrap
     [:h1.mbn "Response"]
     (render-response-formats endpoint)
     (render-response-type (-> endpoint :responses :success))
     (render-response-failures (-> endpoint :responses :failures))]]
   [:div.aside
    [:div.wrap
     (render-sample-responses (-> endpoint :responses :success :samples))]]])

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

(defn create-pages [endpoints]
  (->> [{:id :get-user-id-userId-email-logins
         :path "/user/{email}/logins"
         :api-path "/api/2/user/{email}/logins"
         :method "GET"
         :name "List logins"
         :description "An **awesome** way to look up logins"
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
                               :type [:login_attempt]
                               :samples {:json "[{\"status\": \"true\",
  \"referer\": \"https://stage.payment.schibsted.no/login\",
  \"clientId\": \"[Your client ID]\",
  \"trackingRef\": false,
  \"userId\": [0, 0, 0, 1, 3, 2],
  \"userAgent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36\",
  \"hash\": \"93bd98f00b52f3d3e26fe6852091d1c2\",
  \"trackingTag\": false,
  \"merchantId\": \"[Your merchant ID]\",
  \"created\": \"2014-02-20 09:35:34\",
  \"email\": \"user@domain.tld\",
  \"type\": \"normal\",
  \"initialReferer\": \"https://stage.payment.schibsted.no/account/summary\",
  \"provider\": \"default\",
  \"id\": \"150005945\",
  \"ip\": \"127.0.0.1\"}]"
                                         :jsonp "callback([{\"status\": \"true\",
  \"referer\": \"https://stage.payment.schibsted.no/login\",
  \"clientId\": \"[Your client ID]\",
  \"trackingRef\": false,
  \"userId\": [0, 0, 0, 1, 3, 2],
  \"userAgent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36\",
  \"hash\": \"93bd98f00b52f3d3e26fe6852091d1c2\",
  \"trackingTag\": false,
  \"merchantId\": \"[Your merchant ID]\",
  \"created\": \"2014-02-20 09:35:34\",
  \"email\": \"user@domain.tld\",
  \"type\": \"normal\",
  \"initialReferer\": \"https://stage.payment.schibsted.no/account/summary\",
  \"provider\": \"default\",
  \"id\": \"150005945\",
  \"ip\": \"127.0.0.1\"}])"}}
                     :failures [{:status 401
                                 :description "Your server access token is missing or invalid. Try re-authenticating."
                                 :type :error}
                                {:status 403
                                 :description "You're not allowed to create users. Contact SPiD to be granted access."
                                 :type :error}
                                {:status 418
                                 :description "Why are you contacting your teapot instead of the SPiD servers? Silly rabbit."
                                 :type :error}]}}]
       (map (juxt endpoint-url #(partial create-page %)))
       (into {})))
