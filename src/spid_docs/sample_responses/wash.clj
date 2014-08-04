(ns spid-docs.sample-responses.wash
  (:require [clojure.set :as set]
            [spid-docs.homeless :refer [update-existing update-vals]]))

(def rand-digit (partial rand-int 10))

(def scramble-numbers #(apply str (repeatedly (count %) rand-digit)))

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
   [:oauthToken] "0123456789abcdef0123456789abcdef01234567"
   [:ip] "127.0.0.1"
   [:terms] "[HTML with the current SPiD terms]"
   [:emails] (fn [emails]
               (map-indexed #(assoc %2 :value (str "user@domain" (inc %1) ".tld")) emails))
   [:addresses] mask-addresses
   [:clientRef] "[Your client ID]"))

(defn- list-like-map? [data]
  (and (map? data)
       (seq data)
       (every? map? (vals data))
       (seq (apply set/intersection (map (comp set keys) (vals data))))))

(defn- take-entry-with-most-keys [data]
  (->> data
       (sort-by (fn [[k v]] (- (count (keys v)))))
       (take 1)
       (into {})))

(defn wash-data [data]
  (cond
   (list-like-map? data) (-> data
                             take-entry-with-most-keys
                             mask-sensitive-data
                             (update-vals wash-data))
   (map? data) (-> data
                   mask-sensitive-data
                   (update-vals wash-data))
   (seq? data) (->> data (take 1) (map wash-data))
   (vector? data) (->> data (take 1) (map wash-data))
   :else data))
