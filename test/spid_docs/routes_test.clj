(ns spid-docs.routes-test
  (:require [spid-docs.routes :refer :all]
            [midje.sweet :refer :all]))

(fact "It generates type paths only for types with descriptions"
      (type-path {:id :string}) => nil
      (type-path {:id :datetime :description "ISO-8601"}) => "/types/datetime/")

(fact "It generates endpoint paths"
      (endpoint-path {:path "/logins" :method "GET"}) => "/endpoints/GET/logins/")
