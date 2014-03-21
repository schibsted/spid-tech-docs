(ns spid-docs.pages.endpoint-pages-test
  (:require [clojure.string :as str]
            [hiccup-find.core :refer [hiccup-find hiccup-text]]
            [hiccup.page :refer [html5]]
            [midje.sweet :refer :all]
            [spid-docs.pages.endpoint-pages :refer :all]))

(defn- param [name & options]
  (merge {:name name
          :required? true
          :type :query
          :description "Ok"} (into {} options)))

(fact
 (render-category
  {:category {:section "Identity Management"}}) => [:a.mod.category.small.faded.mbn
                                                    {:href "/#identity-management"}
                                                    "Identity Management"])

(fact (render-title {:method "GET" :path "/logins"}) => [:h1.mbn "GET /logins"])

(fact (render-authentication
       {:requires-authentication? true
        :access-token-types [:server]}) => [:p.small.faded.mtn
                                            "Requires authentication with "
                                            [:strong "server"]
                                            " access token."])

(fact (render-authentication
       {:requires-authentication? true
        :access-token-types ["server", "user"]}) => [:p.small.faded.mtn
                                                     "Requires authentication with "
                                                     [:strong "server or user"]
                                                     " access token."])

(fact (render-authentication
       {:requires-authentication? false
        :access-token-types []}) => nil)

(fact "Renders parameters ordered: path, required, optional"
      (->> (render-request-parameters [(param "zzz" {:required? true :type :path})
                                       (param "login" {:required? false})
                                       (param "id" {:required? true})
                                       (param "userId" {:required? true :type :path})
                                       (param "email" {:required? false})])
           (hiccup-find [:.param :.name])
           (map second)) => '("zzz" "userId" "id" "login" "email"))

(fact "Renders pagination parameters compactly"
      (let [param-row
            (->> (render-request-parameters [] [(param "offset" {:required? false})
                                                (param "limit" {:required? false})
                                                (param "since" {:required? false})
                                                (param "until" {:required? false})])
                 (hiccup-find [:.param])
                 first)]
        (second param-row) => {:id "pagination"}
        (hiccup-text param-row) => "offset, limit, since, and until\nPagination"))

(fact "Renders filter parameter"
      (let [filter-row
            (->> (render-request-parameters [] [] [{:name "merchant", :description "merchant desc", :default? false}])
                 (hiccup-find [:.param])
                 first)]
        (second filter-row) => {:id "filter"}
        (hiccup-text filter-row) => "filter\noptional\nmerchant\nmerchant desc"))

(fact "Highlights path parameters"
      (->> (render-request-parameters [(param "userId" {:required? true :type :path})])
           (hiccup-find [:.param :.required])
           first
           second) => "required path parameter")

(fact "Renders parameter descriptions as markdown"
      (->> (render-request-parameters [(param "login" {:description "*Rly?*"})])
           (hiccup-find [:.desc])
           (map second)) => '("<em>Rly?</em>"))

(fact "Renders example requests"
      (->> (render-request-examples {:path "/endpoints"
                                     :api-path "/api/2/endpoints"
                                     :parameters [(param "login" {:required? false})]
                                     :access-token-types ["server"]})
           (hiccup-find [:.tab-content])
           first
           (hiccup-find [:pre])
           count) => 2) ;; Minimal example, and maximal example with all parameters

(fact "Renders examples with no optional parameters without minimal/maximal distinction"
      (let [examples (render-request-examples {:path "/endpoints"
                                               :api-path "/api/2/endpoints"
                                               :parameters [(param "login" {:required? true})]
                                               :access-token-types ["server"]})]
        (->> examples
             (hiccup-find [:.tab-content])
             first
             (hiccup-find [:pre])
             count)) => 1)

(defn text [node]
  (str/join "" (rest node)))

(fact "Enumerates response formats in a human style (with Oxford comma)"
      (text (render-response-formats {:response-formats [:json]})) =>
      "This endpoint supports the <a href=\"/formats/#json\">JSON</a> response format"

      (text (render-response-formats {:response-formats [:json :jsonp]})) =>
      "This endpoint supports the <a href=\"/formats/#json\">JSON</a> and <a href=\"/formats/#jsonp\">JSON-P</a> response formats"

      (text (render-response-formats {:response-formats [:json :jsonp :xml]})) =>
      "This endpoint supports the <a href=\"/formats/#json\">JSON</a>, <a href=\"/formats/#jsonp\">JSON-P</a>, and <a href=\"/formats/#xml\">XML</a> response formats")

(fact
 (let [hiccup (render-response-type {:status 200 :description "**Hey**"} [])]
   (first hiccup) => [:h2 "Success: 200 OK"]
   (second hiccup) => "<p><strong>Hey</strong></p>"))

(fact "Renders inline types inline"
      (->>
       (render-response-type {:status 200 :description "**Hey**"}
                             {:login {:id :login
                                      :inline? true
                                      :fields [{:name "id" :type :string}]}})
       (hiccup-find [:table])
       count) => 1)

(fact
 (->>
  (render-response-failures [{:status 404 :description "No"}
                             {:status 405 :description "WAT"}])
  (hiccup-find [:li :span])
  (map hiccup-text)) => '("No" "WAT"))

(fact "Renders json sample response before jsonp"
      (->> (render-sample-responses {:jsonp "JSONP" :json "JSON"})
           (hiccup-find [:.tab-content])
           (map hiccup-text)) => '("JSON" "JSONP"))
