(ns spid-docs.sample-responses.definitions
  (:require [clojure.data.codec.base64 :as base64]
            [clojure.data.json :as json]
            [spid-client-clojure.core :as spid]
            [spid-docs.config :as config]
            [spid-docs.sample-responses.defsample :refer [defsample]]))

(defn base64-encode [str]
  (String. (base64/encode (.getBytes str)) "UTF-8"))

(defsample GET "/endpoints")

(defsample GET "/describe/{object}" {:object "User"})

(defsample GET "/status")

(defsample GET "/version")

(defsample GET "/terms")

(defsample GET "/email/{email}/status" {:email (base64-encode "john@doe.com")})

(defsample GET "/phone/{phone}/status" {:phone (base64-encode "+46701111111")})

(defsample GET "/clients")

(defsample GET "/logins")

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

(defsample GET "/digitalcontents")
(defsample GET "/kpis")
(defsample GET "/terms")
(defsample GET "/me")
(defsample GET "/logout")
