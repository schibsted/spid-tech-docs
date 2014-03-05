(ns spid-docs.confluence-test
  (:require [midje.sweet :refer :all]
            [spid-docs.confluence :refer :all]
            [spid-docs.formatting :refer [to-html]]))

(fact (:body (to-storage-format "<p>paragraph</p>")) => "<p>paragraph</p>")

(fact (:body (to-storage-format (to-html "External [link](http://example.com)")))
      => "<p>External <a href=\"http://example.com\">link</a></p>")

(fact (:body (to-storage-format (to-html "Page [link](#Working%20examples)")))
      => "<p>Page <ac:link ac:anchor=\"Working examples\"><ac:plain-text-link-body><![CDATA[link]]></ac:plain-text-link-body></ac:link></p>")

(fact (:body (to-storage-format (to-html "Site [link](/getting-started/)")
                                {"/getting-started/" (fn [] {:title "Getting started with the SPiD API"})}))
      => "<p>Site <ac:link><ri:page ri:content-title=\"Getting started with the SPiD API\"></ri:page><ac:plain-text-link-body><![CDATA[link]]></ac:plain-text-link-body></ac:link></p>")

(fact (:body (to-storage-format (to-html "```php\n<?php echo 1; ?>\n```")))
      => "<ac:structured-macro ac:name=\"code\"><ac:parameter ac:name=\"language\">php</ac:parameter><ac:plain-text-body><![CDATA[<?php echo 1; ?>\n]]></ac:plain-text-body></ac:structured-macro>")

(fact
 "Unsupported languages are skipped to avoid confusing Confluence."
 (:body (to-storage-format (to-html "```clj\n(+ 1 2 3)\n```")))
 => "<ac:structured-macro ac:name=\"code\"><ac:plain-text-body><![CDATA[(+ 1 2 3)\n]]></ac:plain-text-body></ac:structured-macro>")

(fact (:body (to-storage-format
              (str "<div class=\"tabs\">"
                     "<h3 class=\"tab\">One</h3>"
                     "<div class=\"tab-content\"><p>Hello</p></div>"
                     "<h3 class=\"tab\">Two</h3>"
                     "<div class=\"tab-content\"><p>Hi there</p></div>"
                   "</div>")))
      => (str "<ac:structured-macro ac:name=\"auitabs\">"
                "<ac:parameter ac:name=\"direction\">horizontal</ac:parameter>"
                "<ac:rich-text-body>"
                  "<ac:structured-macro ac:name=\"auitabspage\">"
                    "<ac:parameter ac:name=\"title\">One</ac:parameter>"
                    "<ac:rich-text-body><p>Hello</p></ac:rich-text-body>"
                  "</ac:structured-macro>"
                  "<ac:structured-macro ac:name=\"auitabspage\">"
                    "<ac:parameter ac:name=\"title\">Two</ac:parameter>"
                    "<ac:rich-text-body><p>Hi there</p></ac:rich-text-body>"
                  "</ac:structured-macro>"
                "</ac:rich-text-body>"
              "</ac:structured-macro>"))

(fact (:body (to-storage-format
              (str "<div class=\"line\">"
                     "<div class=\"unit s1of3\">1</div>"
                     "<div class=\"unit s1of3\">2</div>"
                     "<div class=\"lastUnit\">3</div>"
                   "</div>")))
      => (str "<ac:layout>"
                "<ac:layout-section ac:type=\"three_equal\">"
                  "<ac:layout-cell>1</ac:layout-cell>"
                  "<ac:layout-cell>2</ac:layout-cell>"
                  "<ac:layout-cell>3</ac:layout-cell>"
                "</ac:layout-section>"
              "</ac:layout>"))

(fact (to-storage-format
       "<h1>The title</h1><h2>First section</h2><h3>Second section</h3>")
      => {:title "The title"
          :body "<h1>First section</h1><h2>Second section</h2>"})
