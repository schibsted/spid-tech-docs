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
                      :ip "80.65.52.213"
                      :emails [{:value "some@email.no"} {:value "some-other@email.no"}]
                      :addresses {:home {:country "NORGE"
                                         :streetNumber "6"
                                         :longitude ""
                                         :floor ""
                                         :locality "OSLO"
                                         :formatted "VARGVEIEN 6, 0139 OSLO, NORGE"
                                         :streetEntrance ""
                                         :apartment ""
                                         :postalCode "0139"
                                         :latitude ""
                                         :type "home"
                                         :region ""
                                         :streetAddress "VARGVEIEN"}
                                  :office {:country "NORGE"
                                           :streetNumber "32"
                                           :longitude ""
                                           :floor ""
                                           :locality "OSLO"
                                           :formatted "Annen gate 32, 0139 OSLO, NORGE"
                                           :streetEntrance ""
                                           :apartment ""
                                           :postalCode "0139"
                                           :latitude ""
                                           :type "home"
                                           :region ""
                                           :streetAddress "VARGVEIEN"}}}
                     mask-sensitive-data)]
        (:clientId data) => "[Your client ID]"
        (:merchantId data) => "[Your merchant ID]"
        (:userId data) => #(not (= % "0123456"))
        (:email data) => "user@domain.tld"
        (:ip data) => "127.0.0.1"
        (:emails data) => [{:value "user@domain1.tld"} {:value "user@domain2.tld"}]
        (:addresses data) => {:home {:country "NORGE"
                                     :streetNumber "1"
                                     :longitude ""
                                     :floor ""
                                     :locality "OSLO"
                                     :formatted "STREET 1, 0123 OSLO, NORGE"
                                     :streetEntrance ""
                                     :apartment ""
                                     :postalCode "0123"
                                     :latitude ""
                                     :type "home"
                                     :region ""
                                     :streetAddress "STREET"}
                              :office {:country "NORGE"
                                       :streetNumber "2"
                                       :longitude ""
                                       :floor ""
                                       :locality "OSLO"
                                       :formatted "STREET 2, 0123 OSLO, NORGE"
                                       :streetEntrance ""
                                       :apartment ""
                                       :postalCode "0123"
                                       :latitude ""
                                       :type "home"
                                       :region ""
                                       :streetAddress "STREET"}}))

(fact "It processes sample response"
      (-> {:code "201" :data {:clientId "666" :url "http://vg.no"}}
          process-sample-response ) => {:sample-response
                                        {:status "201"
                                         :response "{\"clientId\":\"[Your client ID]\", \"url\":\"http://vg.no\"}\n"}})
