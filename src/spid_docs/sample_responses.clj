(ns spid-docs.sample-responses
  "Tools to generate sample responses for all documentet endpoints. Atually uses
   the API to ensure that sample responses reflect the API, and then runs the
   result through a series of functions that anonymizes and scrambles
   potentially sensitive data."
  (:require [clojure.data.json :as json]
            [clojure.set :refer [rename-keys]]
            [spid-docs.api-client :as api]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [update-existing]])
  (:import java.util.Date))

(def scramble-numbers #(map rand-int (range (count %))))

(defn mask-address
  "Anonymize addresses "
  [num address]
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
  (with-out-str (json/pprint (->> response :data process-data) :escape-slash false)))

(defn- get-filename [endpoint]
  (.toLowerCase (str "resources/sample-responses/"
                     (to-id-str (:path endpoint)) "-"
                     (name (:method endpoint)) ".json")))

(defn- generate-sample-response-from-response [endpoint response]
  (let [filename (get-filename endpoint)
        sample (process-sample-response response)]
    (spit filename sample)
    sample))

(defn- json-parse-data [response]
  (assoc response :data (:data (json/read-json (:data response)))))

(defn- ensure-get [endpoint]
  (if (not (= (:method endpoint) :GET))
    (throw (Exception. (str (name (:method endpoint)) " user sample response not implemented")))))

(defn- demo-user-sample [endpoint]
  (ensure-get endpoint)
  (-> (api/get-config)
      (api/get-login-token)
      (api/user-get (:path endpoint))
      (rename-keys {:body :data :status :code})
      (json-parse-data)))

(defmulti generate-sample-response #(vector (:method %) (:path %)))

(defmethod generate-sample-response [:GET "/me"] [endpoint]
  (generate-sample-response-from-response endpoint (demo-user-sample endpoint)))

(defmethod generate-sample-response :default [endpoint]
  (ensure-get endpoint)
  (generate-sample-response-from-response endpoint (api/GET (:path endpoint))))
