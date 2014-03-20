(ns spid-docs.content-test
  (:require [spid-docs.content :refer :all]
            [midje.sweet :refer :all]))

(fact "Cultivates APIs"
      (let [apis {["Identity Management" "User API"] {:description "Users"}
                  ["Identity Management" "Login API"] {:description "Login"}}
            endpoints [{:path "/logins"
                        :category ["Identity Management" "Login API"]}]]
        (cultivate-content {:apis apis :endpoints endpoints})))
