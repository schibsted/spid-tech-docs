(ns spid-docs.example-code
  (:require [clojure.set :refer [difference]]
            [clojure.string :as str]))

(def examples
  {"email" "johnd@example.com"
   "displayName" "John"
   "name" "John Doe"
   "birthday" "1977-01-31"
   "preferredUsername" "johnd"
   "object" "User"})

(defn- replace-path-parameters [url]
  (str/replace url #"\{([^}]+)\}" (fn [[_ match]] (examples match))))

(defn curl-example-code [{:keys [api-path method access-token-types]} params]
  (apply str "curl https://payment.schibsted.no" (replace-path-parameters api-path)
         (when (= "POST" method) " \\\n   -X POST")
         (when (seq access-token-types) " \\\n   -d \"oauth_token=[access token]\"")
         (map (fn [param] (str " \\\n   -d \"" (:name param) "=" (examples (:name param)) "\"")) params)))

(defn clojure-example-code [{:keys [method path]} params]
  (let [sdk-invocation (str "  (sdk/" method " \"" (replace-path-parameters path) "\"" (when (seq params) " {"))
        param-map-indentation (apply str (repeat (count sdk-invocation) " "))]
    (str "(ns example\n  (:require [spid-sdk-clojure.core :as sdk]))\n\n(-> (sdk/create-client \"[client-id]\" \"[secret]\")\n"
         sdk-invocation
         (when (seq params)
           (str (str/join (str "\n" param-map-indentation)
                          (map #(str "\"" (:name %) "\" \"" (examples (:name %)) "\"") params)) "}"))
         "))")))

(defn create-example-code [endpoint]
  (let [params (:parameters endpoint)
        all-params (filter #(= (:type %) :query) params)
        req-params (filter :required? all-params)
        optional-params (difference (set all-params) (set req-params))]
    {:curl {:minimal (curl-example-code endpoint req-params)
            :maximal (when (seq optional-params) (curl-example-code endpoint all-params))}
     :clojure {:minimal (clojure-example-code endpoint req-params)
               :maximal (when (seq optional-params) (clojure-example-code endpoint all-params))}}))
