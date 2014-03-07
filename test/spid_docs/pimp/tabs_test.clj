(ns spid-docs.pimp.tabs-test
  (:require [midje.sweet :refer :all]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.pimp.tabs :refer :all]))

(fact "It understands :tabs"
      (transform-tabs (markdown/parse "
## :tabs My tabs
### :tab One
Hello
### :tab Two
Hi there
## :/tabs"))

      => (str "<h2>My tabs</h2>"
              "<div class=\"tabs\">"
                "<h3 class=\"tab\">One</h3>"
                "<div class=\"tab-content\"><p>Hello</p></div>"
                "<h3 class=\"tab\">Two</h3>"
                "<div class=\"tab-content\"><p>Hi there</p></div>"
              "</div>"))
