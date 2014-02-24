(ns spid-docs.web-test
  (:require [spid-docs.web :refer :all]
            [midje.sweet :refer :all]))

(fact
 "All pages respond with 200 OK"

 (let [urls (keys (get-pages))]
   (doseq [url urls]
     (str url " "(-> (app {:uri url}) :status)) => (str url " " 200))))
