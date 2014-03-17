(ns spid-docs.cultivate.new-endpoints-test
  (:require [midje.sweet :refer :all]
            [spid-docs.cultivate.content-shells :as cs]
            [spid-docs.cultivate.new-endpoints :refer :all]))

(fact (-> (cs/endpoint :path "terms") cultivate-endpoint first :id) => "terms")
(fact (-> (cs/endpoint :path "describe/{object}") cultivate-endpoint first :id) => "describe-object")

(fact
 "Number of returned endpoints depends on the given http methods."

 (-> (cs/endpoint :httpMethods {})
     cultivate-endpoint count) => 0

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method)})
     cultivate-endpoint count) => 1

 (-> (cs/endpoint :httpMethods {:GET (cs/http-method)
                                :POST (cs/http-method)})
     cultivate-endpoint count) => 2)

