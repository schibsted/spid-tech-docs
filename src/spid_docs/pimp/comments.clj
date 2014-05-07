(ns spid-docs.pimp.comments
  (:require [net.cgrand.enlive-html :refer [sniptest html-resource select]]))

(defn- comment-markup [el]
  {:tag :div
   :attrs {:class "section"}
   :content [{:tag :div
              :attrs {:class "main"}
              :content [{:tag :div
                         :attrs {:class "wrap"}
                         :content [{:tag :h2
                                    :content "Comments/feedback"}
                                   {:tag :p
                                    :content "Do you have questions, or just
                                   want to contribute some newly gained insight?
                                   Want to share an example? Please leave a
                                   comment. SPiD reads and responds to every
                                   question. Additionally, your experience can help
                                   others using SPiD, and it can help us
                                   continuously improve our documentation."}
                                   {:tag :div
                                    :attrs {:id "disqus_thread"}}
                                   {:tag :script
                                    :content (str "var disqus_shortname = 'spidtechdocs';
var disqus_identifier = '" (get-in el [:attrs :id]) "';
var disqus_url = 'http://docs.spid.no" (get-in el [:attrs :id]) "';")}]}]}]})

(defn add-comments
  "Add Disqus comments if the page contains a <div id=\"comments\"/>"
  [markup]
  (sniptest markup
            [:div.disqus-comments] comment-markup))

