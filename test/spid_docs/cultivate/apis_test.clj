(ns spid-docs.cultivate.apis-test
  (:require [spid-docs.cultivate.apis :refer :all]
            [midje.sweet :refer :all]))

(fact "Categorizes endpoints"
      (let [apis {["Identity Management" "User API"] {:description "Users"}
                  ["Identity Management" "Login API"] {:description "Login"}}
            endpoints [{:path "/logins" :category {:section "Identity Management" :api "Login API"}}
                       {:path "/me" :category {:section "Identity Management" :api "User API"}}
                       {:path "/logout" :category {:section "Identity Management" :api "Login API"}}
                       {:path "/login" :category {:section "Identity Management" :api "Login API"}}]
            cultivated (cultivate-apis apis endpoints)]
        (-> (cultivated ["Identity Management" "Login API"])
            :endpoints
            count) => 3

        (-> (cultivated ["Identity Management" "Login API"])
            :category) => "Identity Management"

        (-> (cultivated ["Identity Management" "Login API"])
            :api) => "Login API"))
