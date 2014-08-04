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

(comment
  ;; User reference is invalid or does not exists: 238342

  ;; Needs a product
  (defsample [user johndoe]
    POST "/{type}/{id}/{subtype}/{subid}/do/{key}" {:type "user"
                                                    :id (:id user)
                                                    :subtype "product"
                                                    :subid "PID"
                                                    :key "rating"
                                                    :rating "10"})

  ;; Needs a product
  (defsample
    GET "/{type}/{id}/{subtype}/{subid}/do/{key}" {:type "user"
                                                   :id (:id user)
                                                   :subtype "product"
                                                   :subid "PID"
                                                   :key "rating"}))

(comment
  ;; Token is not authorized to access this user.

  (defsample dataobject [user johndoe]
    POST "/user/{id}/dataobject/{key}" {:id (:userId user)
                                        :key "mysetting"
                                        :value "My custom value"})

  (defsample [user johndoe]
    GET "/user/{id}/dataobject/{key}" {:id (:userId user)
                                       :key "mysetting"})

  (defsample [user johndoe]
    GET "/user/{id}/dataobjects" {:id (:userId user)}))

(comment
  ;; No dataObjects found (because the above error)

  (defsample GET "/dataobjects"))

(comment
  ;; Token is not authorized to access this user.

  (defsample [user johndoe]
    DELETE "/user/{id}/dataobject/{key}" {:id (:userId user)
                                          :key "mysetting"}))


(defsample [user johndoe]
  GET "/user/{userId}/trigger/{trigger}" {:userId (:userId user)
                                          :trigger "emailverification"})

(defsample janedoe
  POST "/signup" {:email "jane@doe.com"})

(defsample mycampaign
  POST "/campaign" {:title "My Campaign"})

(defsample GET "/campaigns")

(comment
  (defsample POST "/signup_jwt" {:jwt "TODO"})

  (defsample POST "/attach_jwt" {:jwt "TODO"}))

(comment
  ;; This client is not configured to use Varnish

  (defsample [user johndoe]
    GET "/user/{userId}/varnishId" {:userId (:userId user)}))

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

(comment
  ;; 403 - could it be that the client doesn't have access to the endpoint?

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
                     :currency "NOK"})

  (defsample vgplus-3mo [parent vgplus]
    POST "/product" {:code "vg+3mo"
                     :name "VG+ 3 måneder"
                     :price 9900
                     :vat 2500
                     :paymentOptions 2 ;; "Credit card"
                     :type 2 ;; "Subscription"
                     :currency "NOK"
                     :parentProductId (:productId parent)})

  (defsample vgplus-6mo [parent vgplus]
    POST "/product" {:code "vg+6mo"
                     :name "VG+ 6 måneder"
                     :price 9516
                     :vat 2284
                     :paymentOptions 2 ;; "Credit card"
                     :type 2 ;; "Subscription"
                     :currency "NOK"
                     :parentProductId (:productId parent)})

  (defsample [product vgplus]
    GET "/product/{productId}" {:productId (:productId product)})

  (defsample [product vgplus]
    POST "/product/{productId}" {:productId (:productId product)
                                 :name "VG PLUSS"})

  (defsample [product vgplus]
    GET "/product/{productId}/children" {:productId (:productId product)})

  (defsample [product vgplus]
    GET "/product/{productId}/revisions" {:productId (:productId product)})

  (defsample [product vgplus-3mo]
    GET "/product/{productId}/parents" {:productId (:productId product)})

  (defsample vgplus-bundle
    POST "/product" {:code "vg+bundle"
                     :name "VG+ Alle slag"
                     :price 9516
                     :vat 2284
                     :paymentOptions 2 ;; "Credit card"
                     :type 2 ;; "Subscription"
                     :bundle 1 ;; "Dynamic bundle"
                     :currency "NOK"})

  (defsample [bundle vgplus-bundle
              product vgplus-3mo]
    POST "/bundle/{bundleId}/product/{productId}" {:bundleId (:productId bundle)
                                                   :productId (:productId product)})

  (defsample [bundle vgplus-bundle
              product vgplus-3mo]
    DELETE "/bundle/{bundleId}/product/{productId}" {:bundleId (:productId bundle)
                                                     :productId (:productId product)}))

(comment
  ;; 403
  (defsample vouchers-for-all
    POST "/vouchers/group" {:title "Vouchers for all"
                            :type "8" ;; "Voucher as payment method"
                            :voucherCode "v4a"})

  (defsample [group vouchers-for-all]
    GET "/voucher/{voucherCode}" {:voucherCode (:voucherCode group)})

  (defsample [group vouchers-for-all]
    POST "/vouchers/generate/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

  (defsample [group vouchers-for-all]
    POST "/vouchers/handout/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

  (defsample [user johndoe
              group vouchers-for-all]
    POST "/voucher_handout" {:userId (:userId user)
                             :voucherGroupId (:voucherGroupId group)})

  (defsample GET "/vouchers/groups")

  (defsample [group vouchers-for-all]
    GET "/vouchers/group/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)})

  (defsample [group vouchers-for-all]
    POST "/vouchers/group/{voucherGroupId}" {:voucherGroupId (:voucherGroupId group)}))

(comment
  ;; Må være lagt til i UI-et(?)
  (defsample GET "/identifier/{identifierId}") {:identifierId "???"})

(comment
  ;; 403
  (defsample [user johndoe]
    GET "/agreements/{userId}/payment" {:userId (:userId user)}))

(comment
  ;; 403
  (defsample [user johndoe]
    GET "/logins/{userId}"  {:userId (:userId user)}))

(comment
  ;; 403
  ;; TODO: Må muligens JSON-enkode :items
  (defsample buy-star-wars
    POST "/paylink" {:title "Star Wars Movies"
                     :items [{:description "Star Wars IV"
                              :price 7983
                              :vat 1917}
                             {:description "Star Wars V"
                              :price 7983
                              :vat 1917}
                             {:description "Star Wars VI"
                              :price 7983
                              :vat 1917}]})

  (defsample [paylink buy-star-wars]
    GET "/paylink/{paylinkId}" {:paylinkId (:paylinkId paylink)})

  (defsample [paylink buy-star-wars]
    DELETE "/paylink/{paylinkId}" {:paylinkId (:paylinkId paylink)}))

(comment
  ;; Avhenger av cronjob klokka 05:00 ...
  (defsample GET "/kpis"))

(comment
  ;; Avhenger av kredittkort lagt til på web

  (defsample [user johndoe]
    GET "/user/{userId}/preferences/payment" {:userId (:userId user)})

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

(comment
  ;; Trenger en ordre

  (defsample GET "/orders")
  (defsample GET "/order/{orderId}" {:orderId "???"})
  (defsample GET "/order/{orderId}/transactions" {:orderId "???"})

  (defsample [user johndoe]
    GET "/user/{userId}/orders" {:userId (:userId user)})

  (defsample [user johndoe]
    GET "/user/{userId}/order/{orderId}" {:userId (:userId user)
                                          :orderId "???"})

  (defsample [user johndoe]
    GET "/user/{userId}/transactions" {:userId (:userId user)})

  (defsample GET "/order/{orderId}/items" {:orderId "???"})
  (defsample GET "/order/{orderId}/status" {:orderId "???"})
  (defsample POST "/order/{orderId}/credit" {:orderId "???"
                                             :description "Credit order"})

  (defsample [user johndoe]
    POST "/user/{userId}/order/{orderId}/credit" {:userId (:userId user)
                                                  :orderId "???"})

  (defsample POST "/order/{orderId}/capture" {:orderId "????"})

  (defsample POST "/order/{orderId}/complete" {:orderId "????"})

  (defsample [user johndoe
              product vgplus]
    POST "/user/{userId}/product/{productId}" {:userId (:userId user)
                                               :productId (:productId product)})

  (defsample [user johndoe
              product vgplus]
    GET "/user/{userId}/product/{productId}" {:userId (:userId user)
                                              :productId (:productId product)})

  (defsample [user johndoe]
    GET "/user/{userId}/products" {:userId (:userId user)})

  (defsample [user johndoe
              product vgplus]
    DELETE "/user/{userId}/product/{productId}" {:userId (:userId user)
                                                 :productId (:productId product)})

  (defsample POST "/order/{orderId}/cancel" {:orderId "???"})

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

  (defsample [user johndoe
              subscription vgplus]
    POST "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                         :subscriptionId (:productId subscription)})

  (defsample GET "/subscriptions")

  (defsample [user johndoe
              subscription vgplus]
    DELETE "/user/{userId}/subscription/{subscriptionId}" {:userId (:userId user)
                                                           :subscriptionId (:productId subscription)}))

(defsample GET "/digitalcontents")

(comment
  ;; User token

  (defsample GET "/terms")

  (defsample GET "/me")

  (defsample GET "/me/vouchers")

  (defsample GET "/logout"))
