(ns spid-docs.frontpage
  (:require [clojure.java.io :as io]
            [spid-docs.endpoints :as endpoints]
            [spid-docs.layout :as layout]))

(defn create-page [endpoints]
  {:title "SPiD API Documentation"
   :body (list [:h1 "SPiD API Documentation"]
               (slurp (io/resource "frontpage.html"))
               [:div {:className "group api-reference"}
                [:h2 "API reference"]
                [:p "Just looking for some specific information? Here's an extensive list of all the API endpoints."]
                [:ul (map #(vector :li (list
                                        [:a {:href (endpoints/endpoint-path %)}
                                         (list [:code (:url %)] " - " (:name %))]
                                        [:br]
                                        (:description %))) (:data endpoints))]])})
