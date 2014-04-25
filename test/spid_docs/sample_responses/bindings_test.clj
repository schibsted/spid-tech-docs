(ns spid-docs.sample-responses.bindings-test
  (:require [spid-docs.sample-responses.bindings :refer :all]
            [midje.sweet :refer :all]))

(fact
 (create-local-namespace {:johndoe {:id 1}
                          :janedoe {:id 2}} {'user :janedoe}) => {'user {:id 2}})

(fact
 (resolve-bindings :keyword {} {}) => :keyword
 (resolve-bindings 42 {} {}) => 42
 (resolve-bindings '(42) {} {}) => '(42)
 (resolve-bindings 'user {'user :jane} {:jane 42}) => 42
 (resolve-bindings '(user) {'user :jane} {:jane 42}) => '(42)
 (resolve-bindings {:id 'user} {'user :jane} {:jane 42}) => {:id 42}
 (resolve-bindings {:stuff [13 'user]} {'user :jane} {:jane 42}) => {:stuff [13 42]}
 (-> [13 '(:name user)]
     (resolve-bindings {'user :jane} {:jane {:name "Jane"}})
     eval) => [13 "Jane"])
