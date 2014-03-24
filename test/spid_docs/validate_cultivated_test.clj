(ns spid-docs.validate-cultivated-test
  (:require [midje.sweet :refer :all]
            [spid-docs.content :refer [load-content cultivate-content]]
            [spid-docs.validate-cultivated :refer :all]))

(fact
 "Cultivated data is validated."

 (validate-content (cultivate-content (load-content)))

 "Validation OK" => truthy)
