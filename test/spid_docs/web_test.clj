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
 "All pages respond with 200 OK"

 (let [pages (get-pages)]
   (doseq [url (keys pages)]
     (let [response (app {:uri url})
           status (:status response)]
       ;; This weird comparison is made in order for Midje to show us
       ;; *which* URL fails.
       [url status] => [url 200]

       ;; Link checker
       ;; (doseq [link (-> (:body (app {:uri url}))
       ;;                  java.io.StringReader.
       ;;                  enlive/html-resource
       ;;                  (enlive/select [:a]))]
       ;;   (let [href (get-in link [:attrs :href])]
       ;;     [url href (link-valid? pages href)] => [url href true]))
       ))))
