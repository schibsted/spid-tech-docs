(ns spid-docs.pimp.formatting-test
  (:require [spid-docs.pimp.markdown :refer :all]
            [midje.sweet :refer :all]))

(fact "It gracefully renders markdown"
      (render "### Hey") => "<h3>Hey</h3>"
      (render nil) => "")

(fact "It renders 'inline' markdown without a containing block element"
      (render-inline "Hey *there*") => "Hey <em>there</em>")

