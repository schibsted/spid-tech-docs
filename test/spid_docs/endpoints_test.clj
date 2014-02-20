(ns spid-docs.endpoints-test
  (:require [spid-docs.endpoints :refer :all]
            [midje.sweet :refer :all]))

(fact "It translates templated paths to file names"
      (path-to-filename "logins") => "logins.edn"
      (path-to-filename "logins/{user}") => "logins-user.edn"
      (path-to-filename "logins/{user}/thing") => "logins-user-thing.edn"
      (path-to-filename "api/2/*") => "not-found.edn")
