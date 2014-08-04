(ns spid-docs.sample-responses.defsample-test
  (:require [midje.sweet :refer :all]
            [spid-docs.sample-responses.defsample :refer :all]
            [test-with-files.core :refer [with-tmp-dir tmp-dir]]))

(fact "Build sample dispatches on number and type of arguments"
      (build-sample ['GET "/users"]) => {:method :GET
                                         :route "/users"}

      (build-sample ['POST "/users" {:id 42}]) => {:method :POST
                                                   :route "/users"
                                                   :params {:id 42}}

      (build-sample ['john 'GET "/users"]) => {:id :john
                                               :method :GET
                                               :route "/users"}

      (build-sample ['john 'POST "/users" {:id 42}]) => {:id :john
                                                         :method :POST
                                                         :route "/users"
                                                         :params {:id 42}}

      (build-sample ["Docstring" 'GET "/users"]) => {:docstring "Docstring"
                                                     :method :GET
                                                     :route "/users"}

      (build-sample ["Docstring" 'POST "/users" {:id 42}]) => {:docstring "Docstring"
                                                               :method :POST
                                                               :route "/users"
                                                               :params {:id 42}}

      (build-sample [['user 'john] 'GET "/users"]) => {:dependencies {'user :john}
                                                       :method :GET
                                                       :route "/users"}

      (build-sample [['user 'john] 'POST "/users" {:id 42}]) => {:dependencies {'user :john}
                                                                 :method :POST
                                                                 :route "/users"
                                                                 :params {:id 42}}

      (build-sample ['john "Docstring" 'GET "/users"]) => {:docstring "Docstring"
                                                           :method :GET
                                                           :route "/users"
                                                           :id :john}

      (build-sample ['john "Docstring" 'POST "/users" {:id 42}]) => {:docstring "Docstring"
                                                                     :method :POST
                                                                     :route "/users"
                                                                     :id :john
                                                                     :params {:id 42}}

      (build-sample ['john ['user 'john] 'POST "/users"]) => {:dependencies {'user :john}
                                                              :method :POST
                                                              :route "/users"
                                                              :id :john}

      (build-sample ['john ['user 'john] 'POST "/users" {:id 42}]) => {:dependencies {'user :john}
                                                                       :method :POST
                                                                       :route "/users"
                                                                       :id :john
                                                                       :params {:id 42}}

      (build-sample [['user 'john] "Docstring" 'POST "/users"]) => {:dependencies {'user :john}
                                                                    :docstring "Docstring"
                                                                    :method :POST
                                                                    :route "/users"}

      (build-sample [['user 'john] "Docstring" 'POST "/users" {:id 42}]) => {:dependencies {'user :john}
                                                                             :docstring "Docstring"
                                                                             :method :POST
                                                                             :route "/users"
                                                                             :params {:id 42}}

      (build-sample ['dude ['user 'john] "Docstring" 'POST "/users"]) => {:id :dude
                                                                          :dependencies {'user :john}
                                                                          :docstring "Docstring"
                                                                          :method :POST
                                                                          :route "/users"}

      (build-sample ['dude ['user 'john] "Docstring" 'POST "/users" {:id 42}]) => {:id :dude
                                                                                   :dependencies {'user :john}
                                                                                   :docstring "Docstring"
                                                                                   :method :POST
                                                                                   :route "/users"
                                                                                   :params {:id 42}}

      (-> (build-sample ['dude ['user 'john] "Docstring" 'POST "/users" {:id 42}] *ns*)
          :ns) => *ns*)

(fact "build-sample separates path params and query params"
      (build-sample ['GET "/some/{path}" {:path "somewhere"}]) => {:method :GET
                                                                   :route "/some/{path}"
                                                                   :path-params {:path "somewhere"}
                                                                   :params {}})

(fact "define-sample (defsample's backend) adds sample definitions to list"
      (reset! sample-responses [])

      (define-sample ['GET "/somewhere"])
      @sample-responses => [{:method :GET :route "/somewhere"}]

      (define-sample ['chris 'GET "/users"])
      @sample-responses => [{:method :GET :route "/somewhere"} {:id :chris :method :GET :route "/users"}])

(fact "defsample does not eagerly evaluate params and does not return anything"
      (defsample some-name [user johndoe]
        GET "/path" {:id (:userId user)}) => nil)
