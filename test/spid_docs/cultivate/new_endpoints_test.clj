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

(fact (-> (cs/endpoint :path "terms") cultivate-endpoint first :id) => "terms")
(fact (-> (cs/endpoint :path "describe/{object}") cultivate-endpoint first :id) => "describe-object")

(fact (-> (cs/endpoint :path "terms") cultivate-endpoint first :path) => "/terms")

(fact (-> (cs/endpoint :httpMethods {:GET (cs/http-method)}) cultivate-endpoint
          first :method) => :GET)

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

(fact (-> (cs/endpoint :category ["Insight" "KPI API"]) cultivate-endpoint first :category)
      => {:section "Insight" :api "KPI API"})

(fact (-> (cs/endpoint :pathParameters ["id"]
                       :parameter_descriptions {:id "The user ID"}
                       :alias {:id "user_id"})
          cultivate-endpoint first :parameters)
      => [{:name "id"
           :aliases ["user_id"]
           :description "The user ID"
           :type :path
           :required? true}])

(fact (-> (cs/endpoint :httpMethods {:GET (cs/http-method :required ["email"])}
                       :parameter_descriptions {:email "The email"})
          cultivate-endpoint first :parameters)
      => [{:name "email"
           :description "The email"
           :type :query
           :required? true}])

(fact (-> (cs/endpoint :httpMethods {:POST (cs/http-method :optional ["name"])}
                       :parameter_descriptions {:name "The name"})
          cultivate-endpoint first :parameters)
      => [{:name "name"
           :description "The name"
           :type :query
           :required? false}])

(fact (-> (cs/endpoint :valid_output_formats ["json" "jsonp"])
          cultivate-endpoint first :response-formats)
      => [:json :jsonp])

(fact (-> (cs/endpoint :default_output_format "json")
          cultivate-endpoint first :default-response-format)
      => :json)


