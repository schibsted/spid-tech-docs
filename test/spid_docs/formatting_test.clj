(ns spid-docs.formatting-test
  (:require [spid-docs.formatting :refer :all]
            [midje.sweet :refer :all]))

(fact "It gracefully formats markdown"
      (to-html "### Hey") => "<h3>Hey</h3>"
      (to-html nil) => "")

(fact "It formats 'inline' markdown"
      (line-to-html "Hey *there*") => "Hey <em>there</em>")
