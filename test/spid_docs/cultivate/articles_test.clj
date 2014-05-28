(ns spid-docs.cultivate.articles-test
  (:require [spid-docs.cultivate.articles :refer :all]
            [midje.sweet :refer :all]))

(def relevant-endpoints-example
  "
GET /path/to/{id}
POST /path/to/other/page
")

(fact
 "The entries in :relevant-endpoints are parsed."

 (-> (cultivate-articles {:readme {:body "README"
                                   :relevant-endpoints relevant-endpoints-example}})
     :readme :sections first :relevant-endpoints)

 => [{:method :GET, :path "/path/to/{id}"}
     {:method :POST, :path "/path/to/other/page"}])
