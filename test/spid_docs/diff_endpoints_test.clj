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
     :schema)
 => {:added #{"url"}
     :removed #{"urls"}})

(fact
 "We also check the properties used to describe httpMethods."

 (-> (diff-endpoints [(cs/endpoint
                       {:httpMethods {:GET (-> (cs/http-method)
                                               (dissoc :name)
                                               (assoc :id "x"))}})]
                     [(cs/endpoint
                       {:httpMethods {:GET (cs/http-method)}})])
     :schema)
 => {:removed #{"httpMethods/id"}
     :added #{"httpMethods/name"}})

(fact
 "The new endpoints are validated, to see if the changes are breaking,
  ie. no longer matches our expectations."

 (-> (diff-endpoints [(cs/endpoint)]
                     [(-> (cs/endpoint)
                          (dissoc :url)
                          (assoc :urls []))])
     :breaking-change?)
 => true)

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
 "We also detect any changes to existing endpoints. Note that we don't dig in to
  find out exactly what these changes are."

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

(fact
 "We detect changes in response types."

 (-> (diff-endpoints [(cs/endpoint
                       {:httpMethods {:GET (cs/http-method
                                            {:responses [{:status 200
                                                          :description ""
                                                          :type "object"}]})}})]
                     [(cs/endpoint
                       {:httpMethods {:GET (cs/http-method
                                            {:responses [{:status 200
                                                          :description ""
                                                          :type "string"}]})}})])
     :types)
 => {:removed #{"object"}
     :added #{"string"}})

(fact
 "Response types uses some syntax to signify lists and maps. That's fine, we'll cope."

 (-> (diff-endpoints [(cs/endpoint
                       {:httpMethods {:GET (cs/http-method
                                            {:responses [{:status 200
                                                          :description ""
                                                          :type "[object]"}]})}})]
                     [(cs/endpoint
                       {:httpMethods {:GET (cs/http-method
                                            {:responses [{:status 200
                                                          :description ""
                                                          :type "{id object}"}]})}})])

     :types) => nil) ;; ie, no change in types, since they both refer the 'object' type
