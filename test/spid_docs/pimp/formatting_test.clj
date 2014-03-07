(ns spid-docs.pimp.formatting-test
  (:require [spid-docs.pimp.markdown :refer :all]
            [midje.sweet :refer :all]))

(fact "It gracefully formats markdown"
      (parse "### Hey") => "<h3>Hey</h3>"
      (parse nil) => "")

(fact "It formats 'inline' markdown"
      (inline-parse "Hey *there*") => "Hey <em>there</em>")

