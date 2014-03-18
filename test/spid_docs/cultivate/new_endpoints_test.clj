(ns spid-docs.cultivate.new-endpoints-test
  (:require [midje.sweet :refer :all]
            [spid-docs.cultivate.content-shells :as cs]
            [spid-docs.cultivate.new-endpoints :refer :all]))

(fact
 "Number of returned endpoints depends on the given http methods."

 (-> (cs/endpoint :httpMethods {}) cultivate-endpoint count) => 0
 (-> (cs/endpoint :httpMethods {:GET (cs/http-method)}) cultivate-endpoint count) => 1
 (-> (cs/endpoint :httpMethods {:GET (cs/http-method)
                                :POST (cs/http-method)}) cultivate-endpoint count) => 2)

(fact
 "The path is used to generate a file system friendly identifier."

 (-> (cs/endpoint :path "terms") cultivate-endpoint first :id) => "terms"
 (-> (cs/endpoint :path "describe/{object}") cultivate-endpoint first :id) => "describe-object")

(fact
 "All paths are prefixed with a slash."

 (-> (cs/endpoint :path "terms") cultivate-endpoint first :path) => "/terms"
 (-> (cs/endpoint :path "terms") cultivate-endpoint first :api-path) => "/api/2/terms")

(fact
 "We generate one endpoint for each http verb, so the method is straight on the endpoint."

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method)}) cultivate-endpoint first :method) => :GET)

(fact
 "Names are cut apart and stiched together to fit the httpMethod."

 (-> (cs/endpoint :name "Create subscription") cultivate-endpoint first :name)
 => "Create subscription"

 (->> (cs/endpoint :name "Get or create a product"
                   :httpMethods {:GET (cs/http-method)
                                 :POST (cs/http-method)})
      cultivate-endpoint (map (juxt :method :name)) set)
 => #{[:GET "Get a product"]
      [:POST "Create a product"]}

 (->> (cs/endpoint :name "Create, get or delete an order"
                   :httpMethods {:GET (cs/http-method)
                                 :POST (cs/http-method)
                                 :DELETE (cs/http-method)})
      cultivate-endpoint (map (juxt :method :name)) set)
 => #{[:GET "Get an order"]
      [:POST "Create an order"]
      [:DELETE "Delete an order"]})

(fact
 "Category is more pleasant to use if it the levels are named."

 (-> (cs/endpoint :category ["Insight" "KPI API"]) cultivate-endpoint first :category)
 => {:section "Insight" :api "KPI API"})

(fact
 "Path parameters are included in the general :parameters list.
  They're all required."

 (-> (cs/endpoint :pathParameters ["id"]
                  :parameter_descriptions {:id "The user ID"}
                  :alias {:id "user_id"})
     cultivate-endpoint first :parameters)
 => [{:name "id"
      :aliases ["user_id"]
      :description "The user ID"
      :type :path
      :required? true}])

(fact
 "Query parameters are included in the general :parameters list too."

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method :required ["email"])}
                  :parameter_descriptions {:email "The email"})
     cultivate-endpoint first :parameters)
 => [{:name "email"
      :description "The email"
      :type :query
      :required? true}]

 (-> (cs/endpoint :httpMethods {:POST (cs/http-method :optional ["name"])}
                  :parameter_descriptions {:name "The name"})
     cultivate-endpoint first :parameters)
 => [{:name "name"
      :description "The name"
      :type :query
      :required? false}])

(fact
 "Pagination parameters are treated specially, bundled together and
  removed from the proper parameters list. This is so that readers of
  the documentation can learn pagination once, and then not have to
  sift through it for every endpoint.

  Required pagination parameters are not treated this way."

 (let [endpoint (-> (cs/endpoint :httpMethods {:GET (cs/http-method :optional ["limit" "offset" "since" "until"])})
                    cultivate-endpoint first)]

   (:parameters endpoint) => []
   (:pagination endpoint) => [{:name "limit"
                               :description "Return no more than this number of results."
                               :type :query, :required? false}
                              {:name "offset"
                               :description "Skip this many results."
                               :type :query, :required? false}
                              {:name "since"
                               :description "Return only results on or after this date."
                               :type :query, :required? false}
                              {:name "until"
                               :description "Return only results up to and including this date."
                               :type :query, :required? false}]))

(fact
 "If there are no pagination parameters, the :pagination key is
  removed, instead of it pointing to an empty list."

 (-> (cs/endpoint) cultivate-endpoint first :pagination) => nil)

(fact
 "The filter parameter is also treated specially, since there is a
  list of valid filters for a given endpoint. This too is removed from
  the proper parameters list (if optional)."

 (let [endpoint (-> (cs/endpoint :httpMethods {:GET (cs/http-method :optional ["filters"]
                                                                    :filters ["merchant"]
                                                                    :default_filters [])})
                    cultivate-endpoint first)]

   (:parameters endpoint) => []
   (:filters endpoint) => [{:name "merchant"
                            :description "Show results for entire merchant, not just current client."
                            :default? false}]))

(fact
 "If there is no filter parameter, the :filters key is removed,
  instead of it pointing to an empty list."

 (-> (cs/endpoint) cultivate-endpoint first :filters) => nil)

(fact
 "We prefer response-formats over valid-output-formats since
  'response' is more accurate than 'output', and there is no list of
  'invalid' formats, so the validity is implicit."

 (-> (cs/endpoint :valid_output_formats ["json" "jsonp"])
     cultivate-endpoint first :response-formats)
 => [:json :jsonp])

(fact
 "Likewise, there's a default-response-format instead of a
  default-output-format."

 (-> (cs/endpoint :default_output_format "json")
     cultivate-endpoint first :default-response-format)
 => :json)

(fact
 "We represent the access token types as a set of keys, instead of a
  list, since the data isn't inherently ordered."

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method :access_token_types ["server" "user"])})
     cultivate-endpoint first :access-token-types)
 => #{:server :user})

(fact
 "Authentication is required if there is any access token types
  listed."

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method :access_token_types ["server"])})
     cultivate-endpoint first :requires-authentication?)
 => true

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method :access_token_types [])})
     cultivate-endpoint first :requires-authentication?)
 => false)

(fact
 "Responses are sorted into :success and :failure."

 (-> (cs/endpoint :httpMethods
                  {:GET (cs/http-method :responses [{:status 200
                                                     :description "OK"
                                                     :type "string"}
                                                    {:status 422
                                                     :description "NO!"
                                                     :type "string"}
                                                    {:status 500
                                                     :description "NO NO NO!"
                                                     :type "error"}])})
     cultivate-endpoint first :responses)
 => {:success {:status 200
               :description "OK"
               :type :string}
     :failure [{:status 422
                :description "NO!"
                :type :string}
               {:status 500
                :description "NO NO NO!"
                :type :error}]})

(fact (-> (cs/endpoint :deprecated "1.0") cultivate-endpoint first :deprecated) => "1.0")
