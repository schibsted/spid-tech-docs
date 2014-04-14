(ns spid-docs.sample-responses.definitions
  (:require [spid-docs.defsample :refer [defsample]]))
;; Usikkert
;; POST "/oauth/exchange" (["clientId" true] ["type" true] ["redirectUri" false])

(defsample GET "/endpoints")

(defsample GET "/describe/{object}")

(defsample GET "/status")

(defsample GET "/version")

(defsample GET "/terms")

(defsample GET "/email/john@doe.com/status")

(defsample GET "/clients")

(defsample GET "/productsettings")

(defsample GET "/logins") ;; (["ip" false] ["status" false])

;;(defsample POST "/email_templates" {:templates "???"})
;;(defsample GET "/email_templates")

;; These depend on user (or something else). Q: How to provide the actual data?
;; POST "/{type}/{id}/do/{key}"
;; GET "/{type}/{id}/do/{key}"
;; POST "/{type}/{id}/{subtype}/{subid}/do/{key}"
;; GET "/{type}/{id}/{subtype}/{subid}/do/{key}"

(defsample GET "/reports/dumps")
;;(defsample GET "/reports/dump/{id}")
;;(defsample GET "/reports/template/{template}")

(defsample johndoe
  POST "/user" {:email "john@doe.com" :displayName "John Doe" :name "John Doe"})

(defsample POST "/user/{id}/dataobject/{key}")
(defsample GET "/user/{id}/dataobject/{key}")
(defsample GET "/user/{id}/dataobjects")
(defsample GET "/dataobjects")
(defsample DELETE "/user/{id}/dataobject/{key}")

(defsample GET "/user/{userId}/trigger/{trigger}")

(defsample POST "/signup") ;(email, [password, redirectUri])

(defsample POST "/signup_jwt") ;(jwt)

(defsample POST "/attach_jwt") ;(jwt)

(defsample GET "/user/{userId}/varnishId")

(defsample GET "/user/{userId}")

(defsample GET "/users") ;; ([fields, sort, id, userId, email, emails, emails_regex, displayName, givenName, familyName, fullName, preferredUsername, phoneNumber, phoneNumbers, gender, birthday, address_formatted, address_postalCode, address_country, address_region, address_locality, address_streetAddress, orderId, paymentIdentifier]

(defsample GET "/search/users") ;; ([clientId, query, id, userId, email, displayName, givenName, familyName, fullName, preferredUsername, gender, birthyear, emails, phoneNumber, phoneNumbers, address, homeAddress, invoiceAddress, deliveryAddress, status, /search/users/{query}, query, clientId])

(defsample GET "/user/{userId}/logins")          ;([ip, status])

(defsample POST "/user/{userId}/traits") ;("traits")
(defsample GET "/user/{userId}/traits") ;(["key"])
(defsample DELETE "/user/{userId}/trait/{trait}") ;("trait, "trait")

(defsample POST "/user/{userId}") ;([displayName, name, birthday, addresses, gender, photo, preferredUsername, url, utcOffset]

(defsample GET "/anonymous/users")

;; Products

(defsample POST "/product") ;; (code, name, price, vat, paymentOptions, type, currency, [bundle, hideItems, status, parentProductId, description, url, quantityLimit, saleStart, saleStop, availableStart, availableStop, allowMultiSales, subscriptionPeriod, subscriptionRenewPrice, subscriptionRenewPeriod, subscriptionAutoRenew, subscriptionAutoRenewLockPeriod, subscriptionAutoRenewDisabled, subscriptionGracePeriod, subscriptionEmailReceiptLimit, subscriptionFinalEndDate, subscriptionSurveyUrl])

;; POST igjen for å legge til children
(defsample GET "/products") ;; ([productId, name, code, price, parentProductId, sort])
(defsample GET "/product/{id}")
(defsample POST "/product/{id}") ;; ([name, description, price, vat, paymentOptions, url, quantityLimit, status, availableStart, availableStop, saleStart, saleStop, hideItems, allowMultiSales, subscriptionPeriod, subscriptionRenewPrice, subscriptionRenewPeriod, subscriptionAutoRenew, subscriptionAutoRenewLockPeriod, subscriptionAutoRenewDisabled, subscriptionGracePeriod, subscriptionEmailReceiptLimit, subscriptionFinalEndDate, subscriptionSurveyUrl])

(defsample GET "/product/{productId}/children")

;; Krever at produktet er endret, tror jeg
(defsample GET "/product/{productId}/revisions")

(defsample GET "/products/parents")

;; Må lage bundle først?
(defsample POST "/bundle/{bundleId}/product/{productId}") ;; ([sort, price, vat])
(defsample DELETE "/bundle/{bundleId}/product/{productId}")

(defsample POST "/vouchers/group") ;; (title, type, [campaignId, productId, description" false] ["unique" false] ["voucherCode" false])

(defsample GET "/voucher/{voucherCode}")            ;; (["voucherCode" true])
(defsample POST "/vouchers/generate/{voucherGroupId}") ;; (["voucherGroupId" true] ["amount" false])
(defsample POST "/vouchers/handout/{voucherGroupId}") ;; (["voucherGroupId" true] ["amount" false])
(defsample POST "/voucher_handout") ;; (["userId" true] ["voucherGroupId" true])
(defsample GET "/vouchers/groups") ;; (["title" false] ["campaignId" false] ["type" false] ["productId" false])
(defsample GET "/vouchers/group/{voucherGroupId}") ;; (["voucherGroupId" true])
(defsample POST "/vouchers/group/{voucherGroupId}") ;; (["voucherGroupId" true] ["title" false] ["description" false])

;; Må være lagt til i UI-et(?)
(defsample GET "/identifier/{identifierId}")

(defsample GET "/agreements/{userId}/payment")
(defsample GET "/logins/{userId}")

(defsample POST "/paylink") ;; (["title" true] ["items" true] ["clientReference" false] ["purchaseFlow" false] ["paymentOptions" false] ["expires" false] ["redirectUri" false] ["cancelUri" false] ["sellerUserId" false] ["buyerUserId" false])
(defsample GET "/paylink/{paylinkId}") ;; (["id" true])
(defsample DELETE "/paylink/{paylinkId}")        ;; (["id" true])

;; Avhenger av cronjob klokka 05:00 ...
(defsample GET "/kpis") ;; (["name" false] ["sort" false])

;; Avhenger av kredittkort lagt til på web
(defsample GET "/user/{userId}/preferences/payment")
(defsample POST "/user/{userId}/charge") ;; (["userId" true] ["hash" true] ["requestReference" true] ["items" true] ["clientReference" false] ["paymentOptions" false] ["purchaseFlow" false] ["sellerUserId" false] ["tag" false] ["type" false])

;; Hvordan _lage_ en order??

(defsample GET "/orders") ;; (["sort" false] ["userId" false] ["status" false] ["ocr" false] ["orderId" false] ["clientReference" false])
(defsample GET "/order/{orderId}")  ;; (["orderId" true])
(defsample GET "/order/{orderId}/transactions") ;; (["orderId" true])

(defsample GET "/user/{userId}/orders") ;; (["userId" true] ["status" false])
(defsample GET "/user/{userId}/order/{orderId}") ;; (["userId" true] ["orderId" true])
(defsample GET "/user/{userId}/transactions") ;; (["userId" true])

(defsample GET "/order/{orderId}/items")                  ;; (["orderId" true])
(defsample GET "/order/{orderId}/status")                           ;; (["orderId" true])
(defsample POST "/order/{orderId}/credit") ;; (["orderId" true] ["description" true] ["amount" false] ["orderItemId" false] ["notifyUser" false])

(defsample POST "/user/{userId}/order/{orderId}/credit") ;; (["userId" true] ["orderId" true])

(defsample POST "/order/{orderId}/capture") ;; (["orderId" true] ["amount" false] ["orderItemId" false] ["description" false])
(defsample POST "/order/{orderId}/complete")         ;; (["orderId" true])

(defsample POST "/user/{userId}/product/{productId}")
(defsample  GET "/user/{userId}/product/{productId}")
(defsample GET "/user/{userId}/products")
(defsample DELETE "/user/{userId}/product/{productId}")

(defsample POST "/order/{orderId}/cancel") ;; (["orderId" true])

(defsample POST "/user/{userId}/subscription") ;; (["userId" true] ["productId" true] ["startDate" false] ["orderId" false] ["expires" false])
(defsample GET "/user/{userId}/subscription/{subscriptionId}")
(defsample GET "/user/{userId}/subscriptions")
(defsample POST "/user/{userId}/subscription/{subscriptionId}") ;; ("autoRenew")
(defsample GET "/subscriptions")
(defsample DELETE "/user/{userId}/subscription/{subscriptionId}")

(defsample GET "/digitalcontents")

;; Med user token
;; GET "/terms" (med oath_token, not_accepted)
;; GET "/me"
;; GET "/me/vouchers"
;; GET "/logout" ()

;; Med admin token (vet lite om disse)
;; GET "/injectable/names"
;; POST "/injectable/import" (tokenName, content)

;; Dunno

;; (defsample GET "/campaign/{campaignId}") ;; (["campaignId" true])
;; (defsample POST "/campaign/{campaignId}") ;; (["campaignId" true] ["title" false] ["description" false] ["metaData" false] ["status" false] ["startDate" false] ["stopDate" false] ["additionalReceiptInfo" false] ["requireAddress" false] ["requireVoucher" false] ["products" false])
;; (defsample POST "/campaign") ;; (["title" true] ["description" false] ["metaData" false] ["startDate" false] ["stopDate" false] ["additionalReceiptInfo" false] ["requireAddress" false] ["requireVoucher" false] ["products" false])
;; (defsample GET "/campaigns") ;; (["sort" false] ["campaignId" false] ["title" false] ["description" false] ["status" false])
;; (defsample GET "/product/{productId}/campaigns") ;; (["productId" true])

;; Deprecated
(defsample POST "/payment") ;; (["action" true] ["productId" true] ["clientRef" true] ["agreementRef" true] ["hash" true] ["price" false])
(defsample GET "/payment/{id}")       ;; (["id" true])
(defsample GET "/payments") ;; (["clientRef" false] ["userId" false] ["productId" false] ["status" false])
