(ns spid-docs.sample-responses.definitions
  (:require [clojure.data.codec.base64 :as base64]
            [clojure.data.json :as json]
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

;; Java SDK Client has no DELETE method at this point
;; (defsample [user johndoe]
;;     DELETE "/user/{id}/dataobject/{key}" {:id (:userId user)
;;                                           :key "mysetting"})

(defsample [user johndoe]
  GET "/user/{userId}/trigger/{trigger}" {:userId (:userId user)
                                          :trigger "emailverification"})

(defsample janedoe
  POST "/signup" {:email "jane@doe.com"})

(defsample mycampaign
  POST "/campaign" {:title "My Campaign"})

(defsample GET "/campaigns")

(comment
  ;; Figure out to create an acceptable JWT

  (defsample POST "/signup_jwt" {:jwt "TODO"})

  (defsample POST "/attach_jwt" {:jwt "TODO"}))

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

;; No DELETE yet
;; (defsample [bundle vgplus-bundle
;;             product vgplus-3mo]
;;   DELETE "/bundle/{bundleId}/product/{productId}" {:bundleId (:productId bundle)
;;                                                    :productId (:productId product)})

(defsample vouchers-for-all
  POST "/vouchers/group" {:title "Vouchers for all"
                          :type "8" ;; "Voucher as payment method"
                          :voucherCode "VOUCH4A"
                          :unique 1})

(defsample GET "/vouchers/groups")

(defsample [group vouchers-for-all]
  GET "/vouchers/group/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

(defsample [group vouchers-for-all]
  POST "/vouchers/group/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)
                                           :title "Vouchers for everyone!"})

(defsample [group vouchers-for-all]
  POST "/vouchers/generate/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

(defsample [group vouchers-for-all]
  POST "/vouchers/handout/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

(comment
  ;; 500
  (defsample [user johndoe
              group vouchers-for-all]
    POST "/voucher_handout" {:userId (:userId user)
                             :voucherGroupId (:voucherGroupId group)})

  ;; 404
  (defsample
    GET "/voucher/{voucherCode}" {:voucherCode "VOUCH4A"}))

;; Identifiers can only be added by the user. If all data is cleared, log in as
;; the user, and purchase the product created above using one of the test credit
;; cards. Then manually find the identifier ID in Ambassador and paste it here.
;; So much for automation...

(defsample GET "/identifier/{identifierId}" {:identifierId "2054"})

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

(comment
  ;; DELETE
  (defsample [paylink buy-star-wars-link]
    DELETE "/paylink/{paylinkId}" {:paylinkId (:paylinkId paylink)}))


(defsample [user johndoe]
  GET "/user/{userId}/preferences/payment" {:userId (:userId user)})

(comment
  ;; Avhenger av kredittkort lagt til på web

  ;; TODO: Legg til hash (verified hash)
  ;; TODO: Muligens JSON-enkode :items
  (defsample [user johndoe]
    POST "/user/{userId}/charge" {:userId (:userId user)
                                  :requestReference "ref"
                                  :items [{:description "Star Wars IV"
                                           :price 7983
                                           :vat 1917}
                                          {:description "Star Wars V"
                                           :price 7983
                                           :vat 1917}
                                          {:description "Star Wars VI"
                                           :price 7983
                                           :vat 1917}]
                                  :hash "??? verified hash av params"}))

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

(comment
  ;; Update API client
  (defsample [user johndoe
              product themovie]
    DELETE "/user/{userId}/product/{productId}" {:userId (:userId user)
                                                 :productId (:productId product)})

  ;; 412
  (defsample [order first-order]
    POST "/order/{orderId}/cancel" {:orderId (:orderId order)}))

(defsample [user johndoe
            product vgplus]
  POST "/user/{userId}/subscription" {:userId (:userId user)
                                      :productId (:productId product)})

(defsample [user johndoe
            subscription vgplus]
  GET "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                      :subscriptionId (:productId subscription)})

(defsample [user johndoe]
  GET "/user/{userId}/subscriptions" {:userId (:userId user)})

(comment
  ;; Subscription could not be updated

  (defsample [user johndoe
              subscription vgplus]
    POST "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                         :subscriptionId (:productId subscription)
                                                         :autoRenew 1}))

(defsample GET "/subscriptions")

(comment
  ;; Update API client

  (defsample [user johndoe
              subscription vgplus]
    DELETE "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                           :subscriptionId (:productId subscription)}))

(comment
  ;; 412
  (defsample [order first-order]
    POST "/order/{orderId}/capture" {:orderId (:orderId order)})

  ;; 412
  (defsample [order first-order]
    POST "/order/{orderId}/complete" {:orderId (:orderId order)})

  (defsample [user johndoe
              orders johndoes-orders]
    POST "/user/{userId}/order/{orderId}/credit" {:userId (:userId user)
                                                  :orderId (:orderId (first (vals orders)))}))

(defsample GET "/digitalcontents")
(defsample GET "/kpis")
(defsample GET "/terms")
(defsample GET "/me")
(defsample GET "/me/vouchers")

(comment
  ;; 403

  (defsample GET "/logout"))

(comment
  ;; There seems to be a bug in the SODA API for type = user. This avoided above
  ;; by using type = client, but this is not possible with subtypes (which are
  ;; apparently only supported for users - which does not work).
  ;; Presumably, noone uses this deprecated API in the wild.

  (defsample [user johndoe
              product themovie]
    POST "/{type}/{id}/{subtype}/{subid}/do/{key}" {:type "user"
                                                    :id (:userId user)
                                                    :subtype "product"
                                                    :subid (:productId product)
                                                    :key "rating"
                                                    :rating "10"})

  (defsample [user johndoe
              product themovie]
    GET "/{type}/{id}/{subtype}/{subid}/do/{key}" {:type "user"
                                                   :id (:id user)
                                                   :subtype "product"
                                                   :subid (:productId product)
                                                   :key "rating"}))
