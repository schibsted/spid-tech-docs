(ns spid-docs.pimp.toc-test
  (:require [spid-docs.pimp.toc :refer :all]
            [midje.sweet :refer :all]))

(fact
 "It creates a table of contents from the h2 headers in .main"

 (create-toc (str "<spid-toc></spid-toc>"
                  "<div class=\"main\">"
                    "<h2 id=\"heading-1\">Heading 1</h2>"
                    "<h3>Subheading 1</h3>"
                    "<h2 id=\"heading-2\">Heading 2</h2>"
                    "<h3>Subheading 2</h3>"
                  "</div>"
                  "<div class=\"aside\">"
                    "<h2 id=\"heading-aside\">Heading aside</h2>"
                  "</div>"))
 => (str "<ul>"
           "<li><a href=\"#heading-1\">Heading 1</a></li>"
           "<li><a href=\"#heading-2\">Heading 2</a></li>"
         "</ul>"
         "<div class=\"main\">"
           "<h2 id=\"heading-1\">Heading 1</h2>"
           "<h3>Subheading 1</h3>"
           "<h2 id=\"heading-2\">Heading 2</h2>"
           "<h3>Subheading 2</h3>"
         "</div>"
         "<div class=\"aside\">"
           "<h2 id=\"heading-aside\">Heading aside</h2>"
         "</div>"))
