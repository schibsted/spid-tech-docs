(ns spid-docs.pimp.tabs-test
  (:require [midje.sweet :refer :all]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.pimp.tabs :refer :all]))

(fact "It understands :tabs"
      (transform-tabs (markdown/render "
# :tabs
## :tab One
Hello
## :tab Two
Hi there
# :/tabs"))

      => (str "<div class=\"tabs\">"
                "<h4 class=\"tab\">One</h4>"
                "<div class=\"tab-content\"><p>Hello</p></div>"
                "<h4 class=\"tab\">Two</h4>"
                "<div class=\"tab-content\"><p>Hi there</p></div>"
              "</div>"))
