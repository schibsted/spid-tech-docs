(ns spid-docs.formatting-test
  (:require [spid-docs.formatting :refer :all]
            [midje.sweet :refer :all]))

(fact "Pluralizes words"
      (pluralize "Apple") => "Apples"
      (pluralize "Apple" 0) => "Apples"
      (pluralize "Apple" 1) => "Apple"
      (pluralize "Apple" 2) => "Apples"
      (pluralize "status") => "statuses")

(fact "Enumerates humanely, with Oxford comma"
      (enumerate-humanely ["apple"]) => "apple"
      (enumerate-humanely ["apple" "banana"]) => "apple and banana"
      (enumerate-humanely ["apple" "banana" "grapes"]) => "apple, banana, and grapes"
      (enumerate-humanely ["apple" "banana" "grapes" "pie"]) => "apple, banana, grapes, and pie")
