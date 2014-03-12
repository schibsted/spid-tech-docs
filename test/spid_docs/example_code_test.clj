(ns spid-docs.example-code-test
  (:require [spid-docs.example-code :refer :all]
            [midje.sweet :refer :all]))

(fact
 (let [get-example (create-example-code
                    {:path "status",
                     :method "GET",
                     :required [],
                     :optional [],
                     :url "/api/2/status",
                     :pathParameters []
                     :access_token_types []})
       post-example (create-example-code
                     {:method "POST"
                      :required ["email"]
                      :optional ["displayName"
                                 "name"
                                 "birthday"
                                 "preferredUsername"]
                      :path "user"
                      :url "/api/2/user"
                      :pathParameters []
                      :access_token_types ["server"]})]

   (-> get-example :curl :minimal)
   => "curl https://payment.schibsted.no/api/2/status"

   (-> get-example :curl :maximal)
   => nil

   (-> post-example :curl :minimal)
   => "curl https://payment.schibsted.no/api/2/user \\
   -X POST \\
   -d \"oauth_token=[access token]\" \\
   -d \"email=user@example.com\""

   (-> post-example :curl :maximal)
   => "curl https://payment.schibsted.no/api/2/user \\
   -X POST \\
   -d \"oauth_token=[access token]\" \\
   -d \"email=user@example.com\" \\
   -d \"displayName=John\" \\
   -d \"name=John Doe\" \\
   -d \"birthday=1977-01-31\" \\
   -d \"preferredUsername=johnd\""

   (-> get-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/GET \"/status\"))"

   (-> post-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/POST \"/user\" {\"email\" \"user@example.com\"}))"

   (-> post-example :clojure :maximal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/POST \"/user\" {\"email\" \"user@example.com\"
                     \"displayName\" \"John\"
                     \"name\" \"John Doe\"
                     \"birthday\" \"1977-01-31\"
                     \"preferredUsername\" \"johnd\"}))"))
