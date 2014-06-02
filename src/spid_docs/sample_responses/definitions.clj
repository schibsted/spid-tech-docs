(ns spid-docs.sample-responses.definitions
  (:require [clojure.data.codec.base64 :as base64]
            [clojure.data.json :as json]
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

(comment
  ;; There are no product settings for our account.
  ;; Commented out to avoid a sample response of "[]"
  ;; which would only be noise (and a little embarrasing)

  (defsample GET "/productsettings"))

(defsample GET "/logins")

(defsample GET "/reports/dumps")

(defsample johndoe
  POST "/user" {:email "john@doe.com" :displayName "John Doe" :name "John Doe"})

(comment
  ;; User reference is invalid or does not exists: 238342

  (defsample [user johndoe]
    POST "/{type}/{id}/do/{key}" {:type "User"
                                  :id (:userId user)
                                  :key "rating"
                                  :rating "10"})

  (defsample [user johndoe]
    GET "/{type}/{id}/do/{key}" {:type "User"
                                 :id (:userId user)
                                 :key "rating"})

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
                     :price 4758
                     :vat 1142
                     :paymentOptions 2 ;; "Credit card"
                     :type 1 ;; "Digital contents"
                     :currency "NOK"})

  (defsample vgplus
    POST "/product" {:code "vg+"
                     :name "VG+"
                     :price 4758
                     :vat 1142
                     :paymentOptions 2 ;; "Credit card"
                     :type 2 ;; "Subscription"
                     :currency "NOK"})

  (defsample vgplus-3mo [parent vgplus]
    POST "/product" {:code "vg+3mo"
                     :name "VG+ 3 måneder"
                     :price 4758
                     :vat 1142
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

(defsample [user johndoe]
  GET "/agreements/{userId}/payment" {:userId (:userId user)})

;;;;  (defsample GET "/logins/{userId}")
;;;;
;;;;  (defsample POST "/paylink") ;; (["title" true] ["items" true] ["clientReference" false] ["purchaseFlow" false] ["paymentOptions" false] ["expires" false] ["redirectUri" false] ["cancelUri" false] ["sellerUserId" false] ["buyerUserId" false])
;;;;  (defsample GET "/paylink/{paylinkId}") ;; (["id" true])
;;;;  (defsample DELETE "/paylink/{paylinkId}")        ;; (["id" true])
;;;;
;;;;  ;; Avhenger av cronjob klokka 05:00 ...
;;;;  (defsample GET "/kpis") ;; (["name" false] ["sort" false])
;;;;
;;;;  ;; Avhenger av kredittkort lagt til på web
;;;;  (defsample GET "/user/{userId}/preferences/payment")
;;;;  (defsample POST "/user/{userId}/charge") ;; (["userId" true] ["hash" true] ["requestReference" true] ["items" true] ["clientReference" false] ["paymentOptions" false] ["purchaseFlow" false] ["sellerUserId" false] ["tag" false] ["type" false])
;;;;
;;;;  ;; Hvordan _lage_ en order??
;;;;
;;;;  (defsample GET "/orders") ;; (["sort" false] ["userId" false] ["status" false] ["ocr" false] ["orderId" false] ["clientReference" false])
;;;;  (defsample GET "/order/{orderId}")  ;; (["orderId" true])
;;;;  (defsample GET "/order/{orderId}/transactions") ;; (["orderId" true])
;;;;
;;;;  (defsample GET "/user/{userId}/orders") ;; (["userId" true] ["status" false])
;;;;  (defsample GET "/user/{userId}/order/{orderId}") ;; (["userId" true] ["orderId" true])
;;;;  (defsample GET "/user/{userId}/transactions") ;; (["userId" true])
;;;;
;;;;  (defsample GET "/order/{orderId}/items")                  ;; (["orderId" true])
;;;;  (defsample GET "/order/{orderId}/status")                           ;; (["orderId" true])
;;;;  (defsample POST "/order/{orderId}/credit") ;; (["orderId" true] ["description" true] ["amount" false] ["orderItemId" false] ["notifyUser" false])
;;;;
;;;;  (defsample POST "/user/{userId}/order/{orderId}/credit") ;; (["userId" true] ["orderId" true])
;;;;
;;;;  (defsample POST "/order/{orderId}/capture") ;; (["orderId" true] ["amount" false] ["orderItemId" false] ["description" false])
;;;;  (defsample POST "/order/{orderId}/complete")         ;; (["orderId" true])
;;;;
;;;;  (defsample POST "/user/{userId}/product/{productId}")
;;;;  (defsample  GET "/user/{userId}/product/{productId}")
;;;;  (defsample GET "/user/{userId}/products")
;;;;  (defsample DELETE "/user/{userId}/product/{productId}")
;;;;
;;;;  (defsample POST "/order/{orderId}/cancel") ;; (["orderId" true])
;;;;
;;;;  (defsample POST "/user/{userId}/subscription") ;; (["userId" true] ["productId" true] ["startDate" false] ["orderId" false] ["expires" false])
;;;;  (defsample GET "/user/{userId}/subscription/{subscriptionId}")
;;;;  (defsample GET "/user/{userId}/subscriptions")
;;;;  (defsample POST "/user/{userId}/subscription/{subscriptionId}") ;; ("autoRenew")
;;;;  (defsample GET "/subscriptions")
;;;;  (defsample DELETE "/user/{userId}/subscription/{subscriptionId}")
;;;;
;;;;  (defsample GET "/digitalcontents")
;;;;
;;;;  ;; Med user token
;;;;  ;; GET "/terms" (med oath_token, not_accepted)

(defsample GET "/me")

;;;;  ;; GET "/me/vouchers"
;;;;  ;; GET "/logout" ()
;;;;
;;;;  ;; Med admin token (vet lite om disse)
;;;;  ;; GET "/injectable/names"
;;;;  ;; POST "/injectable/import" (tokenName, content)
;;;;
;;;;  ;; Dunno
;;;;
;;;;  ;; (defsample GET "/campaign/{campaignId}") ;; (["campaignId" true])
;;;;  ;; (defsample POST "/campaign/{campaignId}") ;; (["campaignId" true] ["title" false] ["description" false] ["metaData" false] ["status" false] ["startDate" false] ["stopDate" false] ["additionalReceiptInfo" false] ["requireAddress" false] ["requireVoucher" false] ["products" false])
;;;;  ;; (defsample POST "/campaign") ;; (["title" true] ["description" false] ["metaData" false] ["startDate" false] ["stopDate" false] ["additionalReceiptInfo" false] ["requireAddress" false] ["requireVoucher" false] ["products" false])
;;;;  ;; (defsample GET "/campaigns") ;; (["sort" false] ["campaignId" false] ["title" false] ["description" false] ["status" false])
;;;;  ;; (defsample GET "/product/{productId}/campaigns") ;; (["productId" true])
;;;;
;;;;  ;; Deprecated
;;;;  (defsample POST "/payment") ;; (["action" true] ["productId" true] ["clientRef" true] ["agreementRef" true] ["hash" true] ["price" false])
;;;;  (defsample GET "/payment/{id}")       ;; (["id" true])
;;;;  (defsample GET "/payments") ;; (["clientRef" false] ["userId" false] ["productId" false] ["status" false])
