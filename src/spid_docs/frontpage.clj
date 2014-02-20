(ns spid-docs.frontpage
  (:require [spid-docs.layout :as layout]
            [spid-docs.endpoints :as endpoints]))

(defn create-page [endpoints]
  {:title "Available endpoints"
   :body (list
          [:h1 "SPiD API endpoints documentation"]
          [:ul (map #(vector :li (list
                                  [:a {:href (endpoints/endpoint-path %)}
                                   (list [:code (:url %)] " - " (:name %))]
                                  [:br]
                                  (:description %))) (:data endpoints))])})
