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

(defsample GET "/productsettings")

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

;;(defsample POST "/product")

;; (code, name, price, vat, paymentOptions, type, currency,
;;[bundle, hideItems, status, parentProductId, description, url, quantityLimit,
;;saleStart, saleStop, availableStart, availableStop, allowMultiSales, subscriptionPeriod,
;;subscriptionRenewPrice, subscriptionRenewPeriod, subscriptionAutoRenew,
;;subscriptionAutoRenewLockPeriod, subscriptionAutoRenewDisabled, subscriptionGracePeriod,
;;subscriptionEmailReceiptLimit, subscriptionFinalEndDate, subscriptionSurveyUrl])

;;;;
;;;;  ;; POST igjen for å legge til children
;;;;  (defsample GET "/products") ;; ([productId, name, code, price, parentProductId, sort])
;;;;  (defsample GET "/product/{id}")
;;;;  (defsample POST "/product/{id}") ;; ([name, description, price, vat, paymentOptions, url, quantityLimit, status, availableStart, availableStop, saleStart, saleStop, hideItems, allowMultiSales, subscriptionPeriod, subscriptionRenewPrice, subscriptionRenewPeriod, subscriptionAutoRenew, subscriptionAutoRenewLockPeriod, subscriptionAutoRenewDisabled, subscriptionGracePeriod, subscriptionEmailReceiptLimit, subscriptionFinalEndDate, subscriptionSurveyUrl])
;;;;
;;;;  (defsample GET "/product/{productId}/children")
;;;;
;;;;  ;; Krever at produktet er endret, tror jeg
;;;;  (defsample GET "/product/{productId}/revisions")
;;;;
;;;;  (defsample GET "/products/parents")
;;;;
;;;;  ;; Må lage bundle først?
;;;;  (defsample POST "/bundle/{bundleId}/product/{productId}") ;; ([sort, price, vat])
;;;;  (defsample DELETE "/bundle/{bundleId}/product/{productId}")
;;;;
;;;;  (defsample POST "/vouchers/group") ;; (title, type, [campaignId, productId, description" false] ["unique" false] ["voucherCode" false])
;;;;
;;;;  (defsample GET "/voucher/{voucherCode}")            ;; (["voucherCode" true])
;;;;  (defsample POST "/vouchers/generate/{voucherGroupId}") ;; (["voucherGroupId" true] ["amount" false])
;;;;  (defsample POST "/vouchers/handout/{voucherGroupId}") ;; (["voucherGroupId" true] ["amount" false])
;;;;  (defsample POST "/voucher_handout") ;; (["userId" true] ["voucherGroupId" true])
;;;;  (defsample GET "/vouchers/groups") ;; (["title" false] ["campaignId" false] ["type" false] ["productId" false])
;;;;  (defsample GET "/vouchers/group/{voucherGroupId}") ;; (["voucherGroupId" true])
;;;;  (defsample POST "/vouchers/group/{voucherGroupId}") ;; (["voucherGroupId" true] ["title" false] ["description" false])
;;;;
;;;;  ;; Må være lagt til i UI-et(?)
;;;;  (defsample GET "/identifier/{identifierId}")
;;;;
;;;;  (defsample GET "/agreements/{userId}/payment")
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
;;;;  ;; GET "/me"
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
