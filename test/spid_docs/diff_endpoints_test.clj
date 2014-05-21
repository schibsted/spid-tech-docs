(ns spid-docs.diff-endpoints-test
  (:require [midje.sweet :refer :all]
            [spid-docs.cultivate.content-shells :as cs]
            [spid-docs.diff-endpoints :refer :all]))

(fact
 "Equal lists are equal. No changes detected. Returns nil."

 (diff-endpoints [] []) => nil
 (diff-endpoints [(cs/endpoint)] [(cs/endpoint)]) => nil)

(fact
 "The properties that describe each endpoint (the schema) are checked for
  changes."

 (-> (diff-endpoints [(-> (cs/endpoint)
                          (dissoc :url)
                          (assoc :urls []))]
                     [(cs/endpoint)])
     (dissoc :changed)) ;; not relevant for this test
 => {:schema-change? true
     :schema {:added #{"url"}
              :removed #{"urls"}}})

(fact
 "We also check the properties used to describe httpMethods."

 (-> (diff-endpoints [(cs/endpoint
                       {:httpMethods {:GET (-> (cs/http-method)
                                               (dissoc :name)
                                               (assoc :id "x"))}})]
                     [(cs/endpoint
                       {:httpMethods {:GET (cs/http-method)}})])
     (dissoc :changed)) ;; not relevant for this test
 => {:schema-change? true
     :schema {:removed #{"httpMethods/id"}
              :added #{"httpMethods/name"}}})

(fact
 "The new endpoints are validated, to see if the changes are breaking,
  ie. no longer matches our expectations."

 (-> (diff-endpoints [(cs/endpoint)]
                     [(-> (cs/endpoint)
                          (dissoc :url)
                          (assoc :urls []))])
     (dissoc :changed)) ;; not relevant for this test
 => {:schema-change? true
     :schema {:removed #{"url"}
              :added #{"urls"}}
     :breaking-change? true})

(fact
 "We detect added and removed endpoints, determined by [path method] pairs."

 (diff-endpoints [(cs/endpoint
                   {:path "/path"
                    :httpMethods {:GET (cs/http-method)}})]
                 [(cs/endpoint
                   {:path "/path"
                    :httpMethods {:POST (cs/http-method)}})])
 => {:added #{{:path "/path", :method :POST}}
     :removed #{{:path "/path", :method :GET}}})

(fact
 "We also detect any changes to existing endpoints, but don't report on
  exactly what these changes are."

 (diff-endpoints [(cs/endpoint
                   {:path "/path"
                    :httpMethods {:GET (cs/http-method
                                        {:name "abc"})}})]
                 [(cs/endpoint
                   {:path "/path"
                    :httpMethods {:GET (cs/http-method
                                        {:name "def"})}})])
 => {:changed #{{:path "/path", :method :GET}}})

(fact
 "We detect changes in parameter names."

 (-> (diff-endpoints [(cs/endpoint {:pathParameters ["old-pp" "same-pp"]
                                    :httpMethods {:GET (cs/http-method
                                                        {:optional ["old-opt" "same-opt"]
                                                         :required ["old-req" "same-req"]})}})]
                     [(cs/endpoint {:pathParameters ["new-pp" "same-pp"]
                                    :httpMethods {:GET (cs/http-method
                                                        {:optional ["new-opt" "same-opt"]
                                                         :required ["new-req" "same-req"]})}})])
     :params)
 => {:removed #{"old-opt" "old-pp" "old-req"}
     :added #{"new-opt" "new-pp" "new-req"}})

