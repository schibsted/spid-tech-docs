(ns spid-docs.pages.endpoints-pages-test
  (:require [spid-docs.pages.endpoint-pages :refer :all]
            [midje.sweet :refer :all]))

(fact "It calculates endpoint urls and API urls"
      (endpoint-url {:path "logins"}) => "/endpoints/logins")
