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
                    {:method "GET"
                     :parameters []
                     :path "/status"
                     :api-path "/api/2/status"
                     :access-token-types []
                     :example-params example-params})
       post-example (create-example-code
                     {:method "POST"
                      :parameters [{:name "email" :required? true :type :query}
                                   {:name "displayName" :required? false :type :query}
                                   {:name "name" :required? false :type :query}
                                   {:name "birthday" :required? false :type :query}
                                   {:name "preferredUsername" :required? false :type :query}]
                      :path "/user"
                      :api-path "/api/2/user"
                      :access-token-types ["server"]
                      :example-params example-params})
       param-example (create-example-code
                      {:method "GET"
                       :parameters [{:name "property" :required? false :type :query}
                                    {:name "locale" :required? false :type :query}
                                    {:name "object" :required? true :type :path}]
                       :path "/describe/{object}"
                       :api-path "/api/2/describe/{object}"
                       :access-token-types ["server"]
                       :example-params example-params})]

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

(let [client (sdk/create-client \"[client-id]\" \"[secret]\")
      token (sdk/create-server-token client)]
  (sdk/GET client token \"/status\"))"

   (-> param-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(let [client (sdk/create-client \"[client-id]\" \"[secret]\")
      token (sdk/create-server-token client)]
  (sdk/GET client token \"/describe/User\"))"

   (-> post-example :clojure :minimal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(let [client (sdk/create-client \"[client-id]\" \"[secret]\")
      token (sdk/create-server-token client)]
  (sdk/POST client token \"/user\" {\"email\" \"johnd@example.com\"}))"

   (-> post-example :clojure :maximal)
   => "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(let [client (sdk/create-client \"[client-id]\" \"[secret]\")
      token (sdk/create-server-token client)]
  (sdk/POST client token \"/user\" {\"email\" \"johnd@example.com\"
                                  \"displayName\" \"John\"
                                  \"name\" \"John Doe\"
                                  \"birthday\" \"1977-01-31\"
                                  \"preferredUsername\" \"johnd\"}))"

   (-> get-example :java :minimal)
   => "String responseJSON = sppClient.
    GET(\"/status\").
    getResponseBody();"

   (-> param-example :java :minimal)
   => "String responseJSON = sppClient.
    GET(\"/describe/User\").
    getResponseBody();"

   (-> post-example :java :minimal)
   => "Map<String, String> params = new HashMap<>() {{
    put(\"email\", \"johnd@example.com\");
}};

String responseJSON = sppClient.
    POST(\"/user\", params).
    getResponseBody();"

   (-> post-example :java :maximal)
   => "Map<String, String> params = new HashMap<>() {{
    put(\"email\", \"johnd@example.com\");,
    put(\"displayName\", \"John\");,
    put(\"name\", \"John Doe\");,
    put(\"birthday\", \"1977-01-31\");,
    put(\"preferredUsername\", \"johnd\");
}};

String responseJSON = sppClient.
    POST(\"/user\", params).
    getResponseBody();"

   (-> get-example :php :minimal)
   => "<?php
$client->auth();
echo var_dump($client->api(\"/status\"));"

   (-> param-example :php :minimal)
   => "<?php
$client->auth();
echo var_dump($client->api(\"/describe/User\"));"

   (-> post-example :php :minimal)
   => "<?php
$params = array(
    \"email\" => \"johnd@example.com\"
);

$client->auth();
echo var_dump($client->api(\"/user\", $params));"

   (-> post-example :php :maximal)
   => "<?php
$params = array(
    \"email\" => \"johnd@example.com\",
    \"displayName\" => \"John\",
    \"name\" => \"John Doe\",
    \"birthday\" => \"1977-01-31\",
    \"preferredUsername\" => \"johnd\"
);

$client->auth();
echo var_dump($client->api(\"/user\", $params));")

 (fact "Does not render minimal and maximal when all parameters are required"
       (->
        (create-example-code
         {:method "GET"
          :parameters [{:name "id" :required? true :descripton "Ok" :type :query}]
          :path "/status"
          :api-path "/api/2/status"
          :access-token-types []
          :example-params example-params})
        :clojure
        :maximal) => nil))
