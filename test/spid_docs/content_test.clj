(ns spid-docs.content-test
  (:require [spid-docs.content :refer :all]
            [midje.sweet :refer :all]))

(fact "It translates templated endpoint paths to file names"
      (endpoint-path-to-filename "logins") => "logins.edn"
      (endpoint-path-to-filename "logins/{user}") => "logins-user.edn"
      (endpoint-path-to-filename "logins/{user}/thing") => "logins-user-thing.edn"
      (endpoint-path-to-filename "api/2/*") => "not-found.edn")
