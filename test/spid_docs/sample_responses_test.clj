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
