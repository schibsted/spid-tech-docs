(ns spid-docs.homeless-test
  (:require [spid-docs.sample-responses.defsample :as defsample]
            [spid-docs.homeless :refer :all]
            [midje.sweet :refer :all]))

(fact
 (eval-in-ns '(str "Hey")) => "Hey"
 (eval-in-ns '(str "Hey") (find-ns 'user)) => "Hey"
 (eval-in-ns 'sample-responses
             (find-ns 'spid-docs.sample-responses.defsample)) => defsample/sample-responses)
