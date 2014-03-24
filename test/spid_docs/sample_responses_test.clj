(ns spid-docs.sample-responses-test
  (:require [midje.sweet :refer :all]
            [spid-docs.sample-responses :refer :all]
            [test-with-files.core :refer [with-tmp-dir tmp-dir]]))

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
        (:userId data) => #"^\d{7}$"
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
          process-sample-response ) => "{\"clientId\":\"[Your client ID]\", \"url\":\"http://vg.no\"}\n")

(fact "It generates sample response files."
      (with-tmp-dir
        (with-redefs [spid-docs.api-client/GET (fn [path] {:data {:status 123, :path path}})
                      target-directory tmp-dir]
          (generate-sample-response {:path "/status", :method :GET})
          (slurp (str tmp-dir "/status-get.json")) => "{\"status\":123, \"path\":\"/status\"}\n"
          (slurp (str tmp-dir "/status-get.jsonp")) => "callback({\"status\":123, \"path\":\"/status\"});\n")))
