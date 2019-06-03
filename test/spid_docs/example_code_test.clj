(ns spid-docs.example-code-test
  (:require [midje.sweet :refer :all]
            [spid-docs.example-code :refer :all]))

(def example-params
  {"id" "id"
   "email" "johnd@example.com"
   "displayName" "John"
   "name" "John Doe"
   "birthday" "1977-01-31"
   "preferredUsername" "johnd"
   "property" "status"
   "locale" "nb_NO"
   "object" "User"})

(fact
 (let [get-example (create-example-code
                    {:method :GET
                     :parameters []
                     :path "/status"
                     :api-path "/api/2/status"
                     :access-token-types []
                     :example-params example-params})
       post-example (create-example-code
                     {:method :POST
                      :parameters [{:name "email" :required? true :type :query}
                                   {:name "displayName" :required? false :type :query}
                                   {:name "name" :required? false :type :query}
                                   {:name "birthday" :required? false :type :query}
                                   {:name "preferredUsername" :required? false :type :query}]
                      :path "/user"
                      :api-path "/api/2/user"
                      :access-token-types #{:server}
                      :example-params example-params})
       param-example (create-example-code
                      {:method :GET
                       :parameters [{:name "property" :required? false :type :query}
                                    {:name "locale" :required? false :type :query}
                                    {:name "object" :required? true :type :path}]
                       :path "/describe/{object}"
                       :api-path "/api/2/describe/{object}"
                       :access-token-types #{:server}
                       :example-params example-params})
       user-token-example (create-example-code
                           {:method :GET
                            :parameters [{:name "property" :required? false :type :query}
                                         {:name "locale" :required? false :type :query}
                                         {:name "object" :required? true :type :path}]
                            :path "/me"
                            :api-path "/api/2/me"
                            :access-token-types #{:user}
                            :example-params example-params})]

   (-> get-example :curl :minimal)
   => "curl https://login.schibsted.com/api/2/status -G"

   (-> get-example :curl :maximal)
   => nil

   (-> param-example :curl :minimal)
   => "curl https://login.schibsted.com/api/2/describe/User -G \\
   -H \"Authorization: Bearer [access token]\""

   (-> post-example :curl :minimal)
   => "curl https://login.schibsted.com/api/2/user \\
   -X POST \\
   -H \"Authorization: Bearer [access token]\" \\
   -d \"email=johnd@example.com\""

   (-> post-example :curl :maximal)
   => "curl https://login.schibsted.com/api/2/user \\
   -X POST \\
   -H \"Authorization: Bearer [access token]\" \\
   -d \"email=johnd@example.com\" \\
   -d \"displayName=John\" \\
   -d \"name=John Doe\" \\
   -d \"birthday=1977-01-31\" \\
   -d \"preferredUsername=johnd\"")

 (fact "Does not render minimal and maximal when all parameters are required"
       (->
        (create-example-code
         {:method :GET
          :parameters [{:name "id" :required? true :descripton "Ok" :type :query}]
          :path "/status"
          :api-path "/api/2/status"
          :access-token-types []
          :example-params example-params})
        :clojure
        :maximal) => nil))
