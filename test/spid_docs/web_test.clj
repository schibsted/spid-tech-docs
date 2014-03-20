(ns spid-docs.web-test
  (:require [spid-docs.web :refer :all]
            [midje.sweet :refer :all]))

#_(fact
 "All pages respond with 200 OK"

 (doseq [url (keys (get-pages))]
   (let [status (:status (app {:uri url}))]
     ;; This weird comparison is made in order for Midje to show us
     ;; *which* URL fails.
     [url status] => [url 200])))
