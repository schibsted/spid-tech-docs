(ns spid-docs.defsample-test
  (:require [midje.sweet :refer :all]
            [spid-docs.api-client :refer :all]
            [spid-docs.defsample :refer :all]
            [test-with-files.core :refer [with-tmp-dir tmp-dir]]))

(fact "Build sample dispatches on number and type of arguments"
      (build-sample ['GET "/users"]) => {:method :GET
                                         :path "/users"}

      (build-sample ['POST "/users" {:id 42}]) => {:method :POST
                                                   :path "/users"
                                                   :params {:id 42}}

      (build-sample ['john 'GET "/users"]) => {:id :john
                                               :method :GET
                                               :path "/users"}

      (build-sample ['john 'POST "/users" {:id 42}]) => {:id :john
                                                         :method :POST
                                                         :path "/users"
                                                         :params {:id 42}}

      (build-sample ["Docstring" 'GET "/users"]) => {:docstring "Docstring"
                                                     :method :GET
                                                     :path "/users"}

      (build-sample ["Docstring" 'POST "/users" {:id 42}]) => {:docstring "Docstring"
                                                               :method :POST
                                                               :path "/users"
                                                               :params {:id 42}}

      (build-sample [['user 'john] 'GET "/users"]) => {:dependencies {'user :john}
                                                       :method :GET
                                                       :path "/users"}

      (build-sample [['user 'john] 'POST "/users" {:id 42}]) => {:dependencies {'user :john}
                                                                 :method :POST
                                                                 :path "/users"
                                                                 :params {:id 42}}

      (build-sample ['john "Docstring" 'GET "/users"]) => {:docstring "Docstring"
                                                           :method :GET
                                                           :path "/users"
                                                           :id :john}

      (build-sample ['john "Docstring" 'POST "/users" {:id 42}]) => {:docstring "Docstring"
                                                                     :method :POST
                                                                     :path "/users"
                                                                     :id :john
                                                                     :params {:id 42}}

      (build-sample ['john ['user 'john] 'POST "/users"]) => {:dependencies {'user :john}
                                                              :method :POST
                                                              :path "/users"
                                                              :id :john}

      (build-sample ['john ['user 'john] 'POST "/users" {:id 42}]) => {:dependencies {'user :john}
                                                                       :method :POST
                                                                       :path "/users"
                                                                       :id :john
                                                                       :params {:id 42}}

      (build-sample [['user 'john] "Docstring" 'POST "/users"]) => {:dependencies {'user :john}
                                                                    :docstring "Docstring"
                                                                    :method :POST
                                                                    :path "/users"}

      (build-sample [['user 'john] "Docstring" 'POST "/users" {:id 42}]) => {:dependencies {'user :john}
                                                                             :docstring "Docstring"
                                                                             :method :POST
                                                                             :path "/users"
                                                                             :params {:id 42}}

      (build-sample ['dude ['user 'john] "Docstring" 'POST "/users"]) => {:id :dude
                                                                          :dependencies {'user :john}
                                                                          :docstring "Docstring"
                                                                          :method :POST
                                                                          :path "/users"}

      (build-sample ['dude ['user 'john] "Docstring" 'POST "/users" {:id 42}]) => {:id :dude
                                                                                   :dependencies {'user :john}
                                                                                   :docstring "Docstring"
                                                                                   :method :POST
                                                                                   :path "/users"
                                                                                   :params {:id 42}})

(fact "build-sample separates path params and query params"
      (build-sample ['GET "/some/{path}" {:path "somewhere"}]) => {:method :GET
                                                                   :path "/some/{path}"
                                                                   :path-params {:path "somewhere"}
                                                                   :params {}})

(fact "define-sample (defsample's backend) adds sample definitions to list"
      (reset! sample-responses [])

      (define-sample ['GET "/somewhere"])
      @sample-responses => [{:method :GET :path "/somewhere"}]

      (define-sample ['chris 'GET "/users"])
      @sample-responses => [{:method :GET :path "/somewhere"} {:id :chris :method :GET :path "/users"}])

(fact "Fetches sample response"
      (with-tmp-dir
        (with-redefs [spid-docs.api-client/get-client (fn [] {})
                      spid-docs.api-client/raw-GET (fn [path params] {:body "{\"name\":\"SPP Container\",\"version\":\"0.2\",\"api\":2,\"object\":\"Utility\",\"type\":\"error\",\"code\":404,\"request\":{\"reset\":2656,\"limit\":0,\"remaining\":-5},\"debug\":{\"route\":{\"name\":\"Fallback - 404\",\"url\":\"\\/api\\/*\",\"controller\":\"Api\\/2\\/Utility.notFound\"},\"params\":{\"options\":[],\"where\":[]}},\"meta\":null,\"error\":{\"code\":404,\"type\":\"ApiException\",\"description\":\"No such endpoint.\"},\"data\":null}"
                                                                      :status 404
                                                                      :error {:code 404
                                                                              :type "ApiException"
                                                                              :description "No such endpoint."}
                                                                      :success? false})]
          (fetch-sample-response {:method :GET
                                  :path "/clients"}) => {:method :GET
                                                         :path "/clients"
                                                         :response {:status 404
                                                                    :data nil
                                                                    :error {:code 404
                                                                            :type "ApiException"
                                                                            :description "No such endpoint."}
                                                                    :success? false}})))
