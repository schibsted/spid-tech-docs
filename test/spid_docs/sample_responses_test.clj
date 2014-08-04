(ns spid-docs.sample-responses-test
  (:require [midje.sweet :refer :all]
            [spid-docs.sample-responses :refer :all]
            [test-with-files.core :refer [with-tmp-dir tmp-dir]]))

(fact "It formats sample response"
      (-> {:code "201" :data {:clientId "666" :url "http://vg.no"}}
          format-sample-response ) => "{\n  \"clientId\": \"[Your client ID]\",\n  \"url\": \"http://vg.no\"\n}")

(fact "Makes dependencies available as bindings path params"
      (let [sample-def {:method :GET
                        :route "/somewhere"
                        :path-params '{:name (:name user)}
                        :dependencies {'user :john}}
            loaded-samples [{:id :john
                             :response {:success? true
                                        :data {:name "Dude"}}}]
            interpolated (interpolate-sample-def sample-def loaded-samples)]
        (:path-params interpolated) => {:name "Dude"}))

(fact "Makes dependencies available as bindings for request params"
      (let [sample-def {:method :GET
                        :route "/somewhere"
                        :params '{:name (:name user)}
                        :dependencies {'user :john}}
            loaded-samples [{:id :john
                             :response {:success? true
                                        :data {:name "Dude"}}}]
            interpolated (interpolate-sample-def sample-def loaded-samples)]
        (:params interpolated) => {:name "Dude"}))

(fact "Interpolates parameters in path"
      (let [sample-def {:method :GET
                        :route "/somewhere/{userId}"
                        :path-params {:userId 42}}]
        (:path (interpolate-sample-def sample-def)) => "/somewhere/42"))

(fact "Keeps parameterized path"
      (let [sample-def {:method :GET
                        :route "/somewhere/{userId}"
                        :path-params {:userId 42}}]
        (:route (interpolate-sample-def sample-def)) => "/somewhere/{userId}"))

(fact "Interpolates dependency injected parameters in path"
      (let [sample-def {:method :GET
                        :route "/somewhere/{userId}"
                        :path-params {:userId '(:id user)}
                        :dependencies {'user :john}}
            loaded-samples [{:id :john
                             :response {:data {:id 42}}}]
            interpolated (interpolate-sample-def sample-def loaded-samples)]
        (:path interpolated) => "/somewhere/42"))

(fact "Fetches sample response"
      (with-tmp-dir
        (with-redefs [spid-docs.api-client/get-client (fn [] {})
                      spid-docs.api-client/get-server-token (fn [] {})
                      spid-docs.api-client/GET (fn [token path params]
                                                 {:body "{\"name\":\"SPP Container\",\"version\":\"0.2\",\"api\":2,\"object\":\"Utility\",\"type\":\"error\",\"code\":404,\"request\":{\"reset\":2656,\"limit\":0,\"remaining\":-5},\"debug\":{\"route\":{\"name\":\"Fallback - 404\",\"url\":\"\\/api\\/*\",\"controller\":\"Api\\/2\\/Utility.notFound\"},\"params\":{\"options\":[],\"where\":[]}},\"meta\":null,\"error\":{\"code\":404,\"type\":\"ApiException\",\"description\":\"No such endpoint.\"},\"data\":null}"
                                                  :status 404
                                                  :error {:code 404
                                                          :type "ApiException"
                                                          :description "No such endpoint."}
                                                  :success? false})]
          (fetch-sample-response {:id :john
                                  :method :GET
                                  :access-token-types #{:server}
                                  :path "/clients"
                                  :route "/clients"}) => {:id :john
                                                          :method :GET
                                                          :path "/clients"
                                                          :route "/clients"
                                                          :response {:status 404
                                                                     :data nil
                                                                     :error {:code 404
                                                                             :type "ApiException"
                                                                             :description "No such endpoint."}
                                                                     :success? false}})))

(fact "Fetches sample response for user"
      (with-tmp-dir
        (with-redefs [spid-docs.api-client/get-client (fn [] {})
                      spid-docs.api-client/get-user-token (fn [] {})
                      spid-docs.api-client/GET (fn [token path params]
                                                 {:body "{\"name\":\"SPP Container\",\"version\":\"0.2\",\"api\":2,\"object\":\"Utility\",\"type\":\"error\",\"code\":404,\"request\":{\"reset\":2656,\"limit\":0,\"remaining\":-5},\"debug\":{\"route\":{\"name\":\"Fallback - 404\",\"url\":\"\\/api\\/*\",\"controller\":\"Api\\/2\\/Utility.notFound\"},\"params\":{\"options\":[],\"where\":[]}},\"meta\":null,\"error\":{\"code\":404,\"type\":\"ApiException\",\"description\":\"No such endpoint.\"},\"data\":null}"
                                                  :status 404
                                                  :error {:code 404
                                                          :type "ApiException"
                                                          :description "No such endpoint."}
                                                  :success? false})]
          (fetch-sample-response {:id :john
                                  :method :GET
                                  :access-token-types #{:user}
                                  :path "/me"
                                  :route "/me"}) => {:id :john
                                                     :method :GET
                                                     :path "/me"
                                                     :route "/me"
                                                     :response {:status 404
                                                                :data nil
                                                                :error {:code 404
                                                                        :type "ApiException"
                                                                        :description "No such endpoint."}
                                                                :success? false}})))
