(ns spid-docs.api-client
  "Functions for working with the SPiD API. The SPiD API is used by the
   documentation to get sample responses etc."
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [spid-docs.core :as spid]
            [spid-sdk-clojure.core :as sdk]))

(def config (memoize #(spid/load-edn "config.edn")))

(def get-client (memoize #(let [conf (config)]
                            (sdk/create-client (:client-id conf) (:client-secret conf)))))

(defn GET [path]
  (sdk/GET (get-client) path))

(defn get-endpoints []
  (GET "/endpoints"))

(defn spid-url [config path]
  "Given config (typically from config.edn) and a relative path, like /logins,
   return a full path to the SPiD endpoint"
  (str (:spid-base-url config) path))

(defn user-get [config path]
  "Given config (typically from config.edn) extended with an access
  token (typically from get-login-token), make a request to the specified
  relative path on behalf of the logged in user."
  (http/get (spid-url config (str "/api/2" path "?oauth_token=" (:access-token config)))))

(def access-token (atom nil))

(defn- request-login-token [config]
  (-> (spid-url config "/oauth/token")
      (http/post {:form-params {:client_id (:client-id config)
                                :client_secret (:client-secret config)
                                :grant_type "password"
                                :username (get-in config [:demo-user :email])
                                :password (get-in config [:demo-user :password])
                                :redirect_uri "http://localhost"}})
      :body
      json/read-json))

(defn get-login-token [config]
  "Request an access token on behalf of the demo user, as specified in
  config.edn. Returns a config map extended with the access token. This function
  can safely be called frequently with the value it returns, as this will help
  it cache the access token for the 10 minutes the API allows it to be cached."
  (let [now (.getTime (java.util.Date.))]
    (->> (if (or (nil? @access-token)
                 (> now (:expires @access-token)))
           (let [token (request-login-token config)]
             (reset! access-token (assoc token :expires (+ now (:expires_in token)))))
           @access-token)
         :access_token
         (assoc config :access-token))))
