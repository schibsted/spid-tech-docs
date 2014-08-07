(ns spid-docs.sample-responses.definitions
  (:require [clojure.data.codec.base64 :as base64]
            [clojure.data.json :as json]
            [spid-client-clojure.core :as spid]
            [spid-docs.api-client :as api-client]
            [spid-docs.sample-responses.defsample :refer [defsample]]))

(defn base64-encode [str]
  (String. (base64/encode (.getBytes str)) "UTF-8"))

(defsample GET "/endpoints")

(defsample GET "/describe/{object}" {:object "User"})

(defsample GET "/status")

(defsample GET "/version")

(defsample GET "/terms")

(defsample GET "/email/{email}/status" {:email (base64-encode "john@doe.com")})

(defsample GET "/clients")

(defsample GET "/productsettings")

(defsample GET "/logins")

(defsample GET "/reports/dumps")

;; Once the user is created, it must also be verified. Unfortunately, this
;; cannot be done through the API. If the user is not verified, certain API
;; endpoints will fail with a 403. If running this script from scratch, it must
;; be run in two passes - first to create the user. In this first run, the first
;; endpoint that expects a verified user will crash. Before attempting the
;; second run, you must manually verifiy the user by visiting the URL in the
;; generated email. If you want to regenerate the email to find it more easily
;; in the logs, use GET /user/{userId}/trigger/emailverification
;;
;; Many endpoints will fail with "Token is not authorized to access this user"
;; when trying it with the ID of an unverificated user.

(defsample johndoe
  POST "/user" {:email "john@doe.com" :displayName "John Doe" :name "John Doe"})

(defsample
  POST "/{type}/{id}/do/{key}" {:type "client"
                                :id (:client-id (api-client/get-config))
                                :key "rating"
                                :rating "{\"positive\": 10}"})

(defsample
  GET "/{type}/{id}/do/{key}" {:type "client"
                               :id (:client-id (api-client/get-config))
                               :key "rating"})

(defsample dataobject [user johndoe]
  POST "/user/{id}/dataobject/{key}" {:id (:userId user)
                                      :key "mysetting"
                                      :value "My custom value"})

(defsample [user johndoe]
  GET "/user/{id}/dataobject/{key}" {:id (:userId user)
                                     :key "mysetting"})

(defsample [user johndoe]
  GET "/user/{id}/dataobjects" {:id (:userId user)})

(defsample GET "/dataobjects")

(defsample [user johndoe]
  DELETE "/user/{id}/dataobject/{key}" {:id (:userId user)
                                        :key "mysetting"})

(defsample [user johndoe]
  GET "/user/{userId}/trigger/{trigger}" {:userId (:userId user)
                                          :trigger "emailverification"})

(defsample janedoe
  POST "/signup" {:email "jane@doe.com"})

(defsample mycampaign
  POST "/campaign" {:title "My Campaign"})

(defsample GET "/campaigns")

(comment
  ;; TODO
  ;; Pending documentation from Mikael on using JWT, and on how to generate a
  ;; JWT that will lead to a successful request.
  (defsample POST "/signup_jwt" {:jwt "??"})
  (defsample POST "/attach_jwt" {:jwt "??"}))

(defsample [user johndoe]
  GET "/user/{userId}/varnishId" {:userId (:userId user)})

(defsample [user johndoe]
  GET "/user/{userId}" {:userId (:userId user)})

(defsample GET "/users")

(defsample [user johndoe]
  GET "/search/users" {:email (:email user)})

(defsample [user johndoe]
  GET "/user/{userId}/logins" {:userId (:userId user)})

(defsample trait [user johndoe]
  POST "/user/{userId}/traits" {:userId (:userId user)
                                :traits "{\"key\":\"some-data\"}"})

(defsample [user johndoe]
  GET "/user/{userId}/traits" {:userId (:userId user)})

(defsample [user johndoe]
  DELETE "/user/{userId}/trait/{trait}" {:userId (:userId user)
                                         :trait "key"})

(defsample [user johndoe]
  POST "/user/{userId}" {:userId (:userId user)
                         :name "John Spencer Doe"
                         :addresses (json/write-str {:home {:country "Norway"}})})

(defsample GET "/anonymous/users" {:since "2014-01-01"
                                   :until "2014-04-28"})

;; Products

(defsample themovie
  POST "/product" {:code "themovie"
                   :name "The Movie"
                   :price 9900
                   :vat 2500
                   :paymentOptions 2 ;; "Credit card"
                   :type 1 ;; "Digital contents"
                   :currency "NOK"})

(defsample vgplus
  POST "/product" {:code "vg+"
                   :name "VG+"
                   :price 9900
                   :vat 2500
                   :paymentOptions 2 ;; "Credit card"
                   :type 2 ;; "Subscription"
                   :currency "NOK"
                   :subscriptionPeriod 2592000})

(defsample vgplus-3mo [parent vgplus]
  POST "/product" {:code "vg+3mo"
                   :name "VG+ 3 måneder"
                   :price 9900
                   :vat 2500
                   :paymentOptions 2 ;; "Credit card"
                   :type 2 ;; "Subscription"
                   :currency "NOK"
                   :parentProductId (:productId parent)
                   :subscriptionPeriod 2592000})

(defsample vgplus-6mo [parent vgplus]
  POST "/product" {:code "vg+6mo"
                   :name "VG+ 6 måneder"
                   :price 9516
                   :vat 2284
                   :paymentOptions 2 ;; "Credit card"
                   :type 2 ;; "Subscription"
                   :currency "NOK"
                   :parentProductId (:productId parent)
                   :subscriptionPeriod 2592000})

(defsample [product themovie]
  GET "/product/{id}" {:id (:productId product)})

(defsample [product vgplus]
  POST "/product/{id}" {:id (:productId product)
                        :name "VG PLUSS"})

(defsample [product vgplus]
  GET "/product/{productId}/children" {:productId (:productId product)})

(defsample [product vgplus]
  GET "/product/{productId}/revisions" {:productId (:productId product)})

(defsample
  GET "/products/parents")

(defsample vgplus-bundle
  POST "/product" {:code "vg+bundle"
                   :name "VG+ Alle slag"
                   :price 9516
                   :vat 2284
                   :paymentOptions 2 ;; "Credit card"
                   :type 1 ;; "Product"
                   :bundle 1 ;; "Dynamic bundle"
                   :currency "NOK"})

(defsample [bundle vgplus-bundle
            product vgplus-3mo]
  POST "/bundle/{bundleId}/product/{productId}" {:bundleId (:productId bundle)
                                                 :productId (:productId product)})

(defsample [bundle vgplus-bundle
            product vgplus-3mo]
  DELETE "/bundle/{bundleId}/product/{productId}" {:bundleId (:productId bundle)
                                                   :productId (:productId product)})

(defsample freebies-for-all
  POST "/vouchers/group" {:title "Freebies for all"
                          :type "8" ;; "Voucher as payment method"
                          :voucherCode "F4A"
                          :unique 1})

(defsample GET "/vouchers/groups")

(defsample [group freebies-for-all]
  GET "/vouchers/group/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

(defsample [group freebies-for-all]
  POST "/vouchers/group/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)
                                           :title "Freebies for everyone!"})

(defsample [group freebies-for-all]
  POST "/vouchers/generate/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)
                                              :amount 3})

(defsample [user johndoe
            group freebies-for-all]
  POST "/voucher_handout" {:userId (:userId user)
                           :voucherGroupId (:voucherGroupId group)})

(defsample some-vouchers [group freebies-for-all]
  POST "/vouchers/handout/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)
                                             :amount 1})

(defsample [vouchers some-vouchers]
  GET "/voucher/{voucherCode}" {:voucherCode (:voucherCode (first vouchers))})

(defsample [user johndoe]
  GET "/agreements/{userId}/payment" {:userId (:userId user)})

(defsample [user johndoe]
  GET "/user/{userId}/logins" {:userId (:userId user)})

(defsample buy-star-wars-link
  POST "/paylink" {:title "Star Wars Movies"
                   :redirectUri "http://localhost:8000/callback"
                   :cancelUri "http://localhost:8000/cancel"
                   :clientReference "Order number #3242"
                   :items (json/write-str [{:description "Star Wars IV"
                                            :price 7983
                                            :vat 1917
                                            :quantity 1}
                                           {:description "Star Wars V"
                                            :price 7983
                                            :vat 1917
                                            :quantity 1}
                                           {:description "Star Wars VI"
                                            :price 7983
                                            :vat 1917
                                            :quantity 1}])})

(defsample [paylink buy-star-wars-link]
  GET "/paylink/{paylinkId}" {:paylinkId (:paylinkId paylink)})

(defsample [paylink buy-star-wars-link]
  DELETE "/paylink/{paylinkId}" {:paylinkId (:paylinkId paylink)})

(defsample [user johndoe]
  GET "/user/{userId}/preferences/payment" {:userId (:userId user)})

;; The params to the direct charge API call was generated with:
(comment
  (spid/sign-params {:requestReference "Order #4354"
                     :items (json/write-str [{:name "Star Wars IV"
                                              :price 7983
                                              :vat 1917
                                              :quantity 1}
                                             {:name "Star Wars V"
                                              :price 7983
                                              :vat 1917
                                              :quantity 1}
                                             {:name "Star Wars VI"
                                              :price 7983
                                              :vat 1917
                                              :quantity 1}])}
                    (:client-sign-secret (api-client/get-config))))

;; The user needs to have added a credit card for this to work.

(defsample [user johndoe]
  POST "/user/{userId}/charge"
  {:userId (:userId user)
   :requestReference "Order #4354"
   :hash "qsB0xVTuGkzMIh9NLPGLSmQ33NQ0KxEHFQELLScY1zU"
   :items "[{\"vat\":1917,\"quantity\":1,\"name\":\"Star Wars IV\",\"price\":7983},{\"vat\":1917,\"quantity\":1,\"name\":\"Star Wars V\",\"price\":7983},{\"vat\":1917,\"quantity\":1,\"name\":\"Star Wars VI\",\"price\":7983}]"})

;; Order endpoint sample respones rely on orders having been created outside
;; this script on the client being used

(defsample all-orders GET "/orders")

(defsample first-order [orders all-orders]
  GET "/order/{orderId}" {:orderId (:orderId (first (vals orders)))})

(defsample [order first-order]
  GET "/order/{orderId}/transactions" {:orderId (:orderId order)})

(defsample johndoes-orders [user johndoe]
  GET "/user/{userId}/orders" {:userId (:userId user)})

(defsample [user johndoe
            orders johndoes-orders]
  GET "/user/{userId}/order/{orderId}" {:userId (:userId user)
                                        :orderId (:orderId (first (vals orders)))})

(defsample [user johndoe]
  GET "/user/{userId}/transactions" {:userId (:userId user)})

(defsample [order first-order]
  GET "/order/{orderId}/items" {:orderId (:orderId order)})

(defsample [order first-order]
  GET "/order/{orderId}/status" {:orderId (:orderId order)})

(defsample [order first-order]
  POST "/order/{orderId}/credit" {:orderId (:orderId order)
                                  :description "Credit order"})

(defsample [user johndoe
            product themovie]
  POST "/user/{userId}/product/{productId}" {:userId (:userId user)
                                             :productId (:productId product)})

(defsample [user johndoe
            product themovie]
  GET "/user/{userId}/product/{productId}" {:userId (:userId user)
                                            :productId (:productId product)})

(defsample [user johndoe]
  GET "/user/{userId}/products" {:userId (:userId user)})

(defsample [user johndoe
            product themovie]
  DELETE "/user/{userId}/product/{productId}" {:userId (:userId user)
                                               :productId (:productId product)})

(defsample [user johndoe
            product vgplus]
  POST "/user/{userId}/subscription" {:userId (:userId user)
                                      :productId (:productId product)})

(defsample [user johndoe
            subscription vgplus]
  GET "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                      :subscriptionId (:productId subscription)})

(defsample johndoes-subscriptions [user johndoe]
  GET "/user/{userId}/subscriptions" {:userId (:userId user)})

(defsample [user johndoe
            subscriptions johndoes-subscriptions]
  POST "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                       :subscriptionId (:subscriptionId (first (vals subscriptions)))
                                                       :autoRenew 1})

(defsample GET "/subscriptions")

(defsample [user johndoe
            subscriptions johndoes-subscriptions]
  DELETE "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                         :subscriptionId (:subscriptionId (first (vals subscriptions)))})

(defsample order-to-capture [user johndoe]
  POST "/user/{userId}/charge"
  {:userId (:userId user)
   :requestReference "Order #1111"
   :hash "n6aIU6IMIIItkmHQPQaBKQMKmd8xcoytZwv9rDp6g0M"
   :items "[{\"vat\":1917,\"quantity\":1,\"name\":\"Star Wars IV\",\"price\":7983}]"
   :purchaseFlow "AUTHORIZE"})

(defsample [order order-to-capture]
  POST "/order/{orderId}/capture" {:orderId (:orderId order)})

(defsample [order order-to-capture]
  POST "/order/{orderId}/credit" {:orderId (:orderId order)
                                  :description "Getting the money"})

(defsample order-to-complete [user johndoe]
  POST "/user/{userId}/charge"
  {:userId (:userId user)
   :requestReference "Order #2222"
   :hash "19O-jAErcrmbs7D8yx6cse-V8czPIOUUL1_jdccCTbo"
   :items "[{\"vat\":1917,\"quantity\":1,\"name\":\"Star Wars IV\",\"price\":7983}]"
   :purchaseFlow "AUTHORIZE"})

(defsample [order order-to-complete]
  POST "/order/{orderId}/complete" {:orderId (:orderId order)})

(defsample order-to-cancel [user johndoe]
  POST "/user/{userId}/charge"
  {:userId (:userId user)
   :requestReference "Order #3333"
   :hash "C8h2Dk9GfGpN3TLrbeOufzK2J4zWCRWYBVBny-iLKcA"
   :items "[{\"vat\":1917,\"quantity\":1,\"name\":\"Star Wars IV\",\"price\":7983}]"
   :purchaseFlow "AUTHORIZE"})

(defsample [order order-to-cancel]
  POST "/order/{orderId}/cancel" {:orderId (:orderId order)})

(defsample [order first-order]
  GET "/identifier/{identifierId}" {:identifierId (:identifierId order)})

(defsample GET "/digitalcontents")
(defsample GET "/kpis")
(defsample GET "/terms")
(defsample GET "/me")
(defsample GET "/me/vouchers")
(defsample GET "/logout")
