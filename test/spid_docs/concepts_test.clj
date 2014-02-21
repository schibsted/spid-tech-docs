(ns spid-docs.concepts-test
  (:require [spid-docs.concepts :refer :all]
            [midje.sweet :refer :all]))

(fact "It generates concept paths"
      (concept-path :pagination) => "/concepts/pagination" => "/types/datetime")

(fact "It creates pages for concepts"
      (let [pages (create-pages {"resources/concepts/filters.md" "# Filters"})]
        ((pages "/concepts/filters")) => {:title "Filters"
                                          :body "<h1>Filters</h1>"}))
