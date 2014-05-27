(ns spid-docs.example-code
  (:require [clojure.set :refer [difference]]
            [clojure.string :as str]
            [spid-docs.homeless :refer [update-vals]]))

(defn- get-example [match example-params]
  (or (example-params match)
      (throw (Exception. (str "No example found for parameter '" match "'. Add it to resources/example-params.edn")))))

(defn- replace-path-parameters [url example-params]
  (str/replace url #"\{([^}]+)\}" (fn [[_ match]] (get-example match example-params))))

(defn- exemplify-params [params example-params]
  (map #(vector (name (:name %)) (get-example (:name %) example-params)) params))

(defn- format-params [params tpl & [sep]]
  (str/join (or sep "") (map #(-> tpl
                                  (str/replace #":name" (first %))
                                  (str/replace #":value" (second %))) params)))

(defn curl-example-code [{:keys [api-path method access-token-types example-params]} params]
  (apply str "curl https://payment.schibsted.no" (replace-path-parameters api-path example-params)
         (when (= :POST method) " \\\n   -X POST")
         (when (seq access-token-types) " \\\n   -d \"oauth_token=[access token]\"")
         (format-params (exemplify-params params example-params)
                        " \\\n   -d \":name=:value\"")))

(defn clojure-example-code [{:keys [method path example-params access-token-types]} params]
  (let [sdk-invocation (str "  (spid/" (name method) " client token \"" (replace-path-parameters path example-params) "\"")
        param-map-indentation (apply str (repeat (count sdk-invocation) " "))
        token (if (contains? access-token-types :user)
                "create-user-token client \"[code]\""
                "create-server-token client")]
    (str "(ns example
  (:require [spid-client-clojure.core :as spid]))

(let [client (spid/create-client \"[client-id]\" \"[secret]\")
      token (spid/" token ")]\n"
      sdk-invocation
      (when (seq params)
        (str " {"
             (format-params (exemplify-params params example-params)
                            "\":name\" \":value\""
                            (str "\n  " param-map-indentation)) ; Two additional spaces to account or " {"
             "}"))
      "))")))

(defn- create-params-hash-map [params example-params]
  (when (seq params)
    (str "Map<String, String> params = new HashMap<>() {{\n    "
         (format-params (exemplify-params params example-params) "put(\":name\", \":value\");" ",\n    ")
         "\n}};\n\n")))

(defn java-example-code [{:keys [method path example-params access-token-types]} params]
  (let [has-params (seq params)
        params-hash-map (create-params-hash-map params example-params)
        api-invocation (str (name method) "(token, \"" (replace-path-parameters path example-params) "\""
                            (if has-params (str ", params") "") ")")]
    (str params-hash-map
         (if (contains? access-token-types :user)
           "SpidOAuthToken token = spidClient.getUserToken(code);\n"
           "SpidOAuthToken token = spidClient.getServerToken();\n")
         "String responseJSON = spidClient.\n    " api-invocation ".\n    getResponseBody();")))

(defn- create-params-assoc-array [params example-params]
  (when (seq params)
    (str "$params = array(\n"
         (format-params (exemplify-params params example-params) "    \":name\" => \":value\"" ",\n")
         "\n);\n\n")))

(defn php-example-code [{:keys [method path example-params]} params]
  (let [has-params (seq params)
        params-assoc-array (create-params-assoc-array params example-params)
        api-invocation (str "(\"" (replace-path-parameters path example-params) "\""
                            (when (= :POST method) ", \"POST\"")
                            (when has-params (str ", $params")) ")")]
    (str "<?php\n"
         params-assoc-array
         "$client->auth();\necho var_dump($client->api" api-invocation ");")))

(defn- create-examples-with [ex-fn endpoint req-params optional-params all-params]
  {:minimal (ex-fn endpoint req-params)
   :maximal (when (seq optional-params) (ex-fn endpoint all-params))})

(defn create-example-code [endpoint]
  (let [params (:parameters endpoint)
        all-params (filter #(= (:type %) :query) params)
        req-params (filter :required? all-params)
        optional-params (difference (set all-params) (set req-params))]
    (-> {:curl curl-example-code
         :java java-example-code
         :php php-example-code
         :clojure clojure-example-code}
        (update-vals #(create-examples-with % endpoint req-params optional-params all-params))
        (assoc-in [:java :footnote]
                  "This example is an excerpt, see <a href=\"/endpoints/#java-request-example\">the full example</a>")
        (assoc-in [:php :footnote]
                  "This example is an excerpt, see <a href=\"/endpoints/#php-request-example\">the full example</a>"))))
