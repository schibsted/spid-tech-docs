(ns spid-docs.api-client
  "Functions for working with the SPiD API. The SPiD API is used by the
   documentation to get sample responses etc."
  (:require [spid-client-clojure.core :as sdk]
            [spid-docs.config :refer [get-config]]))

(def get-client (memoize #(let [conf (get-config)]
                            (sdk/create-client (:client-id conf) (:client-secret conf) {:spid-base-url (:spid-base-url conf), :redirect-uri "http://localhost"}))))

(def get-server-token (memoize #(sdk/create-server-token (get-client))))
(def get-user-token (memoize #(let [user (:demo-user (get-config))]
                                (sdk/create-user-token (get-client) (:email user) (:password user)))))

(defn GET [token path & [params]]
  (sdk/GET (get-client) token path params))

(defn POST [token path params]
  (sdk/POST (get-client) token path params))

(defn DELETE [token path params]
  (sdk/DELETE (get-client) token path params))
