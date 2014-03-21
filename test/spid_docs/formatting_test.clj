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

(fact "Turns strings into id-friendly strings"
      (to-id-str "/some/path") => "some-path"
      (to-id-str "/some/{path}") => "some-path"
      (to-id-str "Some sentence") => "some-sentence"
      (to-id-str "Some numbers: 666") => "some-numbers-666")

(fact "Columnizes data structures"
      (columnize [[1] [2] [3] [4]] 2) => [[[1] [3]] [[2] [4]]]
      (columnize [[1] [2 3] [4] [5]] 2) => [[[2 3] [5]] [[1] [4]]]
      (columnize [[1] [2 3] [4] [5 6 7]] 2) => [[[5 6 7] [4]] [[2 3] [1]]]
      (columnize [[1 2 3] [4 5] [6] [7 8] [9 10 11]] 3) => [[[1 2 3] [6]] [[9 10 11]] [[4 5] [7 8]]]
      (columnize [{:things [1 2 3]}
                  {:things [4 5]}
                  {:things [6]}
                  {:things [7 8 9]}
                  {:things [10]}] 2 #(count (:things %))) => [[{:things [1 2 3]}
                                                               {:things [4 5]}],
                                                              [{:things [7 8 9]}
                                                               {:things [6]}
                                                               {:things [10]}]])
