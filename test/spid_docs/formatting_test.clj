(ns spid-docs.formatting-test
  (:require [spid-docs.formatting :refer :all]
            [midje.sweet :refer :all]))

(fact "It formats markdown"
      (to-html "### Hey") => "<h3>Hey</h3>")

(fact "It formats 'inline' markdown"
      (line-to-html "Hey *there*") => "Hey <em>there</em>")
