(ns spid-docs.sample-responses-test
  (:require [spid-docs.sample-responses :refer :all]
            [midje.sweet :refer :all]))

(fact "It reduces lists of data to a manageable amount"
      (process-data [{:id 1} {:id 2} {:id 3}]) => [{:id 1}])

(fact "It masks potentially sensitive data"
      (let [data (-> {:clientId "666"
                      :merchantId "0123456"
                      :userId "0123456"
                      :email "christian@kodemaker.no"
                      :ip "80.65.52.213"}
                     mask-sensitive-data)]
        (:clientId data) => "[Your client ID]"
        (:merchantId data) => "[Your merchant ID]"
        (:userId data) => #(not (= % "0123456"))
        (:email data) => "user@domain.tld"
        (:ip data) => "127.0.0.1"))

(fact "It processes sample response"
      (-> {:code "201" :data {:clientId "666" :url "http://vg.no"}}
          process-sample-response ) => {:sample-response
                                        {:status "201"
                                         :response "{\"clientId\":\"[Your client ID]\", \"url\":\"http://vg.no\"}\n"}})

(facts "It logs in sample user and gets oauth token"
       (fact "It knows where to log in"
             (login-url {:client-id "666"
                         :spid-base-url "https://stage.payment.schibsted.no"}) => "https://stage.payment.schibsted.no/auth/login?client_id=666&response_type=code&redirect_uri=http://localhost")

       (fact "It extracts form params"
             (form-params [{:attrs {:value "https://stage.payment.schibsted.no/auth/start?client_id=52f8e3d9efd04bb749000000&redirect_uri=http%253A%252F%252Flocalhost&response_type=code&flow=signup&product_id=0&campaign_id=0&voucher_code=0&voucher_purchase_code=0&tag=0&webview=0"
                                    :name "success_uri"
                                    :id "signInSuccessUri"
                                    :type "hidden"}}
                           {:attrs {:autocapitalize "off"
                                    :autocorrect "off"
                                    :tabindex "1"
                                    :required "required"
                                    :value ""
                                    :id "identifier"
                                    :name "identifier"
                                    :type "email"}}
                           {:attrs {:spellcheck "false"
                                    :autocorrect "off"
                                    :tabindex "2"
                                    :required "required"
                                    :id "password"
                                    :name "password"
                                    :type "password"}}
                           {:attrs {:checked "checked"
                                    :tabindex "4"
                                    :value "1"
                                    :name "remember"
                                    :id "remember"
                                    :type "checkbox"}}
                           {:attrs {:value ""
                                    :name "extra[q]"
                                    :id "extra_q"
                                    :type "hidden"}}
                           {:attrs {:tabindex "5"
                                    :class "btn submit"
                                    :value "Logg inn"
                                    :type "submit"}}]) => {"success_uri" "https://stage.payment.schibsted.no/auth/start?client_id=52f8e3d9efd04bb749000000&redirect_uri=http%253A%252F%252Flocalhost&response_type=code&flow=signup&product_id=0&campaign_id=0&voucher_code=0&voucher_purchase_code=0&tag=0&webview=0"
                                                           "identifier" ""
                                                           "password" nil
                                                           "remember" "1"
                                                           "extra[q]" ""})

       (fact "It prepares login params"
             (login-params {:demo-user {:email "user@domain.tld"
                                        :password "passWORD"}}
                           [{:attrs {:value "https://stage.payment.schibsted.no/auth/start?client_id=52f8e3d9efd04bb749000000&redirect_uri=http%253A%252F%252Flocalhost&response_type=code&flow=signup&product_id=0&campaign_id=0&voucher_code=0&voucher_purchase_code=0&tag=0&webview=0"
                                     :name "success_uri"
                                     :id "signInSuccessUri"
                                     :type "hidden"}}
                            {:attrs {:autocapitalize "off"
                                     :autocorrect "off"
                                     :tabindex "1"
                                     :required "required"
                                     :value ""
                                     :id "identifier"
                                     :name "identifier"
                                     :type "email"}}
                            {:attrs {:spellcheck "false"
                                     :autocorrect "off"
                                     :tabindex "2"
                                     :required "required"
                                     :id "password"
                                     :name "password"
                                     :type "password"}}
                            {:attrs {:checked "checked"
                                     :tabindex "4"
                                     :value "1"
                                     :name "remember"
                                     :id "remember"
                                     :type "checkbox"}}
                            {:attrs {:value ""
                                     :name "extra[q]"
                                     :id "extra_q"
                                     :type "hidden"}}
                            {:attrs {:tabindex "5"
                                     :class "btn submit"
                                     :value "Logg inn"
                                     :type "submit"}}]) => {"success_uri" "https://stage.payment.schibsted.no/auth/start?client_id=52f8e3d9efd04bb749000000&redirect_uri=http%253A%252F%252Flocalhost&response_type=code&flow=signup&product_id=0&campaign_id=0&voucher_code=0&voucher_purchase_code=0&tag=0&webview=0"
                                                                    "identifier" "user@domain.tld"
                                                                    "password" "passWORD"
                                                                    "remember" "1"
                                                                    "extra[q]" ""}))
