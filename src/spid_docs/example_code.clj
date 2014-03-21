(ns spid-docs.example-code
  (:require [clojure.set :refer [difference]]
            [clojure.string :as str]))

(def examples
  {"action" "???"
   "additionalReceiptInfo" "???"
   "address" "Street"
   "address_country" "Norway"
   "address_formatted" "Street 2, 0123 City, Norway"
   "address_locality" "???"
   "address_postalCode" "0123"
   "address_region" "City"
   "address_streetAddress" "Street"
   "addresses" "???"
   "agreementRef" "???"
   "allowMultiSales" "???"
   "amount" "99"
   "autoRenew" "???"
   "availableStart" "???"
   "availableStop" "???"
   "birthday" "1977-01-31"
   "birthyear" "???"
   "bundle" "???"
   "buyerUserId" "???"
   "campaignId" "???"
   "cancelUri" "???"
   "clientId" "42"
   "clientRef" "???"
   "clientReference" "???"
   "code" "???"
   "content" "???"
   "currency" "NOK"
   "deliveryAddress" "???"
   "description" "???"
   "displayName" "John"
   "email" "johnd@example.com"
   "emails" "???"
   "emails_regex" "???"
   "end_time" "???"
   "expires" "???"
   "familyName" "???"
   "fields" "???"
   "filters" "???"
   "from" "???"
   "fullName" "???"
   "gender" "???"
   "givenName" "???"
   "h" "???"
   "hash" "7374163eed7a0e88f9bf28e128d8da82"
   "hideItems" "???"
   "homeAddress" "???"
   "id" "1337"
   "id/userId/email" "???"
   "invoiceAddress" "???"
   "ip" "???"
   "items" "???"
   "jwt" "???"
   "key" "???"
   "limit" "???"
   "locale" "???"
   "metaData" "???"
   "name" "John Doe"
   "notifyUser" "???"
   "object" "User"
   "ocr" "???"
   "offset" "???"
   "orderId" "???"
   "orderItemId" "???"
   "parentProductId" "???"
   "password" "???"
   "paymentIdentifier" "???"
   "paymentOptions" "???"
   "phoneNumber" "???"
   "phoneNumbers" "???"
   "photo" "???"
   "preferredUsername" "johnd"
   "price" "400"
   "product" "???"
   "productId" "1337"
   "product_id" "???"
   "products" "???"
   "property" "???"
   "purchaseFlow" "???"
   "quantityLimit" "???"
   "query" "???"
   "redeem_limit" "???"
   "redirectUri" "???"
   "requestReference" "???"
   "requireAddress" "???"
   "requireVoucher" "???"
   "role" "???"
   "saleStart" "???"
   "saleStop" "???"
   "section" "???"
   "sellerUserId" "???"
   "since" "???"
   "sort" "???"
   "startDate" "???"
   "start_time" "???"
   "status" "???"
   "stopDate" "???"
   "subid" "???"
   "subscriptionAutoRenew" "???"
   "subscriptionAutoRenewDisabled" "???"
   "subscriptionAutoRenewLockPeriod" "???"
   "subscriptionEmailReceiptLimit" "???"
   "subscriptionFinalEndDate" "???"
   "subscriptionGracePeriod" "???"
   "subscriptionId" "???"
   "subscriptionPeriod" "???"
   "subscriptionRenewPeriod" "???"
   "subscriptionRenewPrice" "???"
   "subscriptionSurveyUrl" "???"
   "subtype" "???"
   "tag" "???"
   "template" "???"
   "templates" "???"
   "title" "???"
   "to" "???"
   "tokenName" "???"
   "trait" "???"
   "traits" "???"
   "trigger" "???"
   "type" "???"
   "unique" "???"
   "until" "???"
   "url" "???"
   "userId" "???"
   "userId/email" "???"
   "utcOffset" "???"
   "value" "???"
   "vat" "???"
   "voucherCode" "???"
   "voucherGroupId" "???"
   "w" "???"})

(defn- replace-path-parameters [url]
  (str/replace url #"\{([^}]+)\}" (fn [[_ match]] (examples match))))

(defn- exemplify-params [params]
  (map #(vector (name (:name %)) (examples (:name %))) params))

(defn- format-params [params tpl & [sep]]
  (str/join (or sep "") (map #(-> tpl
                                  (str/replace #":name" (first %))
                                  (str/replace #":value" (second %))) params)))

(defn curl-example-code [{:keys [api-path method access-token-types]} params]
  (apply str "curl https://payment.schibsted.no" (replace-path-parameters api-path)
         (when (= "POST" method) " \\\n   -X POST")
         (when (seq access-token-types) " \\\n   -d \"oauth_token=[access token]\"")
         (format-params (exemplify-params params)
                        " \\\n   -d \":name=:value\"")))

(defn clojure-example-code [{:keys [method path]} params]
  (let [sdk-invocation (str "    (sdk/" method " \"" (replace-path-parameters path) "\"")
        param-map-indentation (apply str (repeat (count sdk-invocation) " "))]
    (str "(ns example
  (:require [spid-sdk-clojure.core :as sdk]))

(-> (sdk/create-client \"[client-id]\" \"[secret]\")\n"
         sdk-invocation
         (when (seq params)
           (str " {"
                (format-params (exemplify-params params)
                               "\":name\" \":value\""
                               (str "\n  " param-map-indentation)) ; Two additional spaces to account or " {"
                "}"))
         "))")))

(defn- create-params-hash-map [params]
  (when (seq params)
    (str "Map<String, String> params = new HashMap<>() {{\n    "
         (format-params (exemplify-params params) "put(\":name\", \":value\");" ",\n    ")
         "\n}};\n\n")))

(defn java-example-code [{:keys [method path]} params]
  (let [has-params (seq params)
        params-hash-map (create-params-hash-map params)
        api-invocation (str (name method) "(\"" (replace-path-parameters path) "\""
                            (if has-params (str ", params") "") ")")]
    (str params-hash-map
         "String responseJSON = sppClient.\n    " api-invocation ".\n    getResponseBody();")))

(defn- create-params-assoc-array [params]
  (when (seq params)
    (str "$params = array(\n"
         (format-params (exemplify-params params) "    \":name\" => \":value\"" ",\n")
         "\n);\n\n")))

(defn php-example-code [{:keys [method path]} params]
  (let [has-params (seq params)
        params-assoc-array (create-params-assoc-array params)
        api-invocation (str "(\"" (replace-path-parameters path) "\""
                            (if has-params (str ", $params") "") ")")]
    (str params-assoc-array
         "$client->auth();\necho var_dump($client->api" api-invocation ");")))

(defn- create-examples-with [ex-fn endpoint req-params optional-params all-params]
  {:minimal (ex-fn endpoint req-params)
   :maximal (when (seq optional-params) (ex-fn endpoint all-params))})

(defn create-example-code [endpoint]
  (let [params (:parameters endpoint)
        all-params (filter #(= (:type %) :query) params)
        req-params (filter :required? all-params)
        optional-params (difference (set all-params) (set req-params))]
    {:curl (create-examples-with curl-example-code endpoint req-params optional-params all-params)
     :clojure (create-examples-with clojure-example-code endpoint req-params optional-params all-params)
     :java (create-examples-with java-example-code endpoint req-params optional-params all-params)
     :php (create-examples-with php-example-code endpoint req-params optional-params all-params)
     }))
