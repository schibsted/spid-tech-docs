(ns spid-docs.validate-raw-test
  (:require [midje.sweet :refer :all]
            [spid-docs.content :refer [load-content]]
            [spid-docs.validate-raw :refer :all]))

(fact
 "All data is validated."

 (validate-raw-content (load-content))

 "Validation OK" => truthy)
