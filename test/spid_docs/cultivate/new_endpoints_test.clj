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

 (->> (cs/endpoint :name "Get or create subscription"
                   :httpMethods {:GET (cs/http-method)
                                 :POST (cs/http-method)})
      cultivate-endpoint (map (juxt :method :name)) set)
 => #{[:GET "Get subscription"]
      [:POST "Create subscription"]}

 (->> (cs/endpoint :name "Create, get or delete subscription"
                   :httpMethods {:GET (cs/http-method)
                                 :POST (cs/http-method)
                                 :DELETE (cs/http-method)})
      cultivate-endpoint (map (juxt :method :name)) set)
 => #{[:GET "Get subscription"]
      [:POST "Create subscription"]
      [:DELETE "Delete subscription"]})
