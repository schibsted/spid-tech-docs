(ns spid-docs.example-code-test
  (:require [spid-docs.example-code :refer :all]
            [midje.sweet :refer :all]))

(fact
 (let [get-example (create-example-code
                    {:method "GET"
                     :required []
                     :optional []
                     :path "status"
                     :url "/api/2/status"
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
                      :access_token_types ["server"]})
       param-example (create-example-code
                      {:method "GET"
                       :required []
                       :optional ["property" "locale"]
                       :path "describe/{object}"
                       :url "/api/2/describe/{object}"
                       :pathParameters ["object"]
                       :access_token_types ["server"]})]

   (-> get-example :curl :minimal)
   => "curl https://payment.schibsted.no/api/2/status"

   (-> get-example :curl :maximal)
   => nil

   (-> param-example :curl :minimal)
   => "curl https://payment.schibsted.no/api/2/describe/User \\
   -d \"oauth_token=[access token]\""

   (-> post-example :curl :minimal)
   => "curl https://payment.schibsted.no/api/2/user \\
   -X POST \\
   -d \"oauth_token=[access token]\" \\
   -d \"email=johnd@example.com\""

   (-> post-example :curl :maximal)
   => "curl https://payment.schibsted.no/api/2/user \\
   -X POST \\
   -d \"oauth_token=[access token]\" \\
   -d \"email=johnd@example.com\" \\
   -d \"displayName=John\" \\
   -d \"name=John Doe\" \\
   -d \"birthday=1977-01-31\" \\
   -d \"preferredUsername=johnd\""

   (-> get-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/GET \"/status\"))"

   (-> param-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/GET \"/describe/User\"))"

   (-> post-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/POST \"/user\" {\"email\" \"johnd@example.com\"}))"

   (-> post-example :clojure :maximal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")
  (sdk/POST \"/user\" {\"email\" \"johnd@example.com\"
                     \"displayName\" \"John\"
                     \"name\" \"John Doe\"
                     \"birthday\" \"1977-01-31\"
                     \"preferredUsername\" \"johnd\"}))"))
