(ns spid-docs.api-client
  "Functions for working with the SPiD API. The SPiD API is used by the
   documentation to get sample responses etc."
  (:require [spid-docs.load :refer [load-edn]]
            [spid-sdk-clojure.core :as sdk]))

(def get-config (memoize #(load-edn "config.edn")))

(def get-client (memoize #(let [conf (get-config)]
                            (sdk/create-client (:client-id conf) (:client-secret conf) {:spid-base-url (:spid-base-url conf), :redirect-uri "http://localhost"}))))

(def get-token (memoize #(let [client (get-client)] (sdk/create-server-token client))))

(defn GET [path & [params]]
  (sdk/GET (get-client) (get-token) path params))

(defn POST [path params]
  (sdk/POST (get-client) (get-token) path params))

(defn DELETE [path params]
  (sdk/DELETE (get-client) (get-token) path params))
