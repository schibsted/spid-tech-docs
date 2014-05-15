(ns spid-docs.json-test
  (:require [spid-docs.json :refer :all]
            [midje.sweet :refer :all]))

(fact (format-json 42) => "42")
(fact (format-json {:a 42}) => "{\n  \"a\": 42\n}")
(fact (format-json {:a nil}) => "{\n  \"a\": null\n}")
(fact (format-json [1 2 3]) => "[\n  1,\n  2,\n  3\n]")
