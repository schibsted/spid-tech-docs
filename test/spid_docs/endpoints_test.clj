(ns spid-docs.endpoints-test
  (:require [spid-docs.endpoints :refer :all]
            [midje.sweet :refer :all]))

(fact "It calculates endpoint paths and urls"
      (endpoint-url {:path "logins"}) => "/logins"
      (endpoint-path {:path "logins"}) => "/endpoints/logins")

