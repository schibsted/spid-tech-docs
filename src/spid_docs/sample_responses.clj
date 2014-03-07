(ns spid-docs.sample-responses
  "Tools to generate sample responses for all documentet endpoints. Atually uses
   the API to ensure that sample responses reflect the API."
  (:require [clojure.data.json :as json]
            [clojure.set :refer [rename-keys]]
            [spid-docs.api-client :as api]
            [spid-docs.cultivate.endpoints :refer [endpoint-path-to-filename]]
            [spid-docs.homeless :refer [update-existing]])
  (:import java.util.Date))

(def scramble-numbers #(map rand-int (range (count %))))

(defn mask-address [num address]
  (assoc address
    :streetNumber (str (inc num))
    :postalCode "0123"
    :streetAddress "STREET"
    :formatted (str "STREET " (inc num) ", 0123 OSLO, NORGE")))

(defn mask-addresses [addresses]
  (->> addresses
       (map-indexed #(vector (first %2)
                             (mask-address %1 (second %2))))
       (into {})))

(defn mask-sensitive-data [data]
  (update-existing
   data
   [:clientId] "[Your client ID]"
   [:merchantId] "[Your merchant ID]"
   [:userId] scramble-numbers
   [:email] "user@domain.tld"
   [:ip] "127.0.0.1"
   [:emails] (fn [emails]
               (map-indexed #(assoc %2 :value (str "user@domain" (inc %1) ".tld")) emails))
   [:addresses] mask-addresses))

(defn process-data [data]
  (if (map? data)
    (mask-sensitive-data data)
    (->> data
         (take 1)
         (map process-data))))

(defn process-sample-response [response]
  {:sample-response
   {:status (:code response)
    :response (with-out-str (json/pprint (->> response :data process-data) :escape-slash false))}})

(defn- generate-sample-response-from-response [endpoint response]
  (let [filename (str "resources/sample-responses/" (endpoint-path-to-filename endpoint))
        sample (process-sample-response response)]
    (spit filename sample)
    sample))

(defmulti generate-sample-response identity)

(defn- json-parse-data [response]
  (assoc response :data (:data (json/read-json (:data response)))))

(defn- demo-user-sample [endpoint]
  (-> (api/get-config)
      (api/get-login-token)
      (api/user-get (str "/" endpoint))
      (rename-keys {:body :data :status :code})
      (json-parse-data)))

(defmethod generate-sample-response "me" [endpoint]
  (generate-sample-response-from-response endpoint (demo-user-sample endpoint)))

(defmethod generate-sample-response :default [endpoint]
  (generate-sample-response-from-response endpoint (api/GET endpoint)))
