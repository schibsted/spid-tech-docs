(ns spid-docs.web-test
  (:require [clojure.string :as str]
            [midje.sweet :refer :all]
            [net.cgrand.enlive-html :as enlive]
            [spid-docs.web :refer :all]))

(defn link-valid? [pages href]
  (let [path (first (str/split href #"#"))]
    (or
     (not (.startsWith href "/"))
     (contains? pages href)
     (contains? pages (str href "index.html")))))

(fact
 :slow
 "Integration tests. Avoid running them with:

 lein with-profile test midje :autotest :filter -slow"

 (let [pages (get-pages)]
   (doseq [url (keys pages)]
     (let [response (app {:uri url})
           status (:status response)]

       ;; Check that the pages respond with 200 OK.
       [url status] => [url 200]

       ;; Check that all links point to existing pages
       ;; (doseq [link (-> (:body (app {:uri url}))
       ;;                  java.io.StringReader.
       ;;                  enlive/html-resource
       ;;                  (enlive/select [:a]))]
       ;;   (let [href (get-in link [:attrs :href])]
       ;;     [url href (link-valid? pages href)] => [url href true]))
       ))))
