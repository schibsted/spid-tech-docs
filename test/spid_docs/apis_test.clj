(ns spid-docs.apis-test
  (:require [spid-docs.apis :refer :all]
            [midje.sweet :refer :all]))

(fact "It converts keywords to names"
      (get-name :something) => "Something"
      (get-name :something-different) => "Something Different")
