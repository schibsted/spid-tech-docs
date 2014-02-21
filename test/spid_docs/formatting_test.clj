(ns spid-docs.formatting-test
  (:require [spid-docs.formatting :refer :all]
            [midje.sweet :refer :all]))

(fact "It gracefully formats markdown"
      (to-html "### Hey") => "<h3>Hey</h3>"
      (to-html nil) => "")

(fact "It formats 'inline' markdown"
      (line-to-html "Hey *there*") => "Hey <em>there</em>")

(fact "It understands :tabs"
      (to-rich-html "
## :tabs My tabs
### :tab One
Hello
### :tab Two
Hi there
## :/tabs")

      => (str "<h2>My tabs</h2>"
              "<div class=\"tabs\">"
                "<h3 class=\"tab\">One</h3>"
                "<div class=\"tab-content\"><p>Hello</p></div>"
                "<h3 class=\"tab\">Two</h3>"
                "<div class=\"tab-content\"><p>Hi there</p></div>"
              "</div>"))
