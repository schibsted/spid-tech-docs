(ns spid-docs.web-test
  (:require [spid-docs.web :refer :all]
            [midje.sweet :refer :all]))

(fact
 "All pages respond with 200 OK"

 (let [urls (keys (get-pages))]
   (doseq [url urls]
     ;; This weird comparison is made in order for Midje to show us
     ;; *which* URL fails. The (facts ... (fact ...)) forms were not
     ;; helpful with this case.
     (str url " "(-> (app {:uri url}) :status)) => (str url " " 200))))
