(ns spid-docs.pages.endpoints-pages-test
  (:require [hiccup-find.core :refer [hiccup-find]]
            [midje.sweet :refer :all]
            [spid-docs.pages.endpoint-pages :refer :all]))

(defn- param [name & options]
  (merge {:name name
          :required? true
          :type :query
          :description "Ok"} (into {} options)))

(fact "It generates endpoint urls"
      (endpoint-url {:path "/logins" :method "GET"}) => "/endpoints/GET/logins")

(fact
 (render-category
  {:category {:section "Identity Management"}}) => [:a.mod.category.small.faded.mbn
                                                    {:href "#"}
                                                    "Identity Management"])

(fact (render-title {:method "GET" :path "/logins"}) => [:h1.mbn "GET /logins"])

(fact (render-authentication
       {:requires-authentication? true
        :access-token-types ["server"]}) => [:p.small.faded.mtn
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
