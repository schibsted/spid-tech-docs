(ns spid-docs.sample-responses
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [spid-docs.api :as api]
            [spid-docs.content :refer [endpoint-path-to-filename]]
            [spid-docs.enlive :as enlive]))

(defn- update-existing [m & forms]
  (if (-> forms count (mod 2) (= 0) not)
    (throw (Exception. "update-if needs an even number of forms")))
  (->> forms
       (partition 2)
       (reduce (fn [memo [path val]]
                 (if-let [curr (get-in memo path)]
                   (assoc-in memo path (if (fn? val) (val curr) val))
                   memo)) m)))

(def scramble-numbers #(map rand-int (range (count %))))

(defn mask-sensitive-data [data]
  (update-existing
   data
   [:clientId] "[Your client ID]"
   [:merchantId] "[Your merchant ID]"
   [:userId] scramble-numbers
   [:email] "user@domain.tld"
   [:ip] "127.0.0.1"))

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

(defn generate-sample-response [endpoint]
  (let [response (api/GET endpoint)
        filename (str "resources/sample-responses/" (endpoint-path-to-filename endpoint))
        sample (process-sample-response response)]
    (spit filename sample)
    sample))

(defn form-params [inputs]
  (->> inputs
       (map :attrs)
       (filter :name)
       (map #(vector (:name %) (:value %)))
       (into {})))

(defn login-params [config inputs]
  (merge (form-params inputs)
         (-> (:demo-user config)
             (rename-keys {:email "identifier" :password "password"}))))

(defn login-url [config]
  (str (:spid-base-url config) "/auth/login"
       "?client_id=" (:client-id config)
       "&response_type=code"
       "&redirect_uri=http://localhost"))

(defn login [config]
  (->> (login-url config)
       (http/get)
       :body
       (enlive/parse)
       (enlive/select [:input])
       (login-params config)
       (http/post (login-url config))))
