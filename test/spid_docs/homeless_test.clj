(ns spid-docs.homeless-test
  (:require [spid-docs.homeless :refer :all]
            [midje.sweet :refer :all]))

(fact
 (eval-in-ns '(str "Hey")) => "Hey"
 (eval-in-ns '(str "Hey") (find-ns 'user)) => "Hey"
 (eval-in-ns 'sample-responses
             (find-ns 'spid-docs.sample-responses.defsample)) => spid-docs.sample-responses.defsample/sample-responses)
