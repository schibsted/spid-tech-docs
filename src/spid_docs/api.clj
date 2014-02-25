(ns spid-docs.api
  (:require [spid-docs.content :as content]
            [spid-docs.core :as spid]
            [spid-sdk-clojure.core :as sdk]))

(def get-client (memoize (fn []
                           (let [cred (spid/load-edn "credentials.edn")]
                             (sdk/create-client (:client-id cred) (:client-secret cred))))))

(defn GET [path]
  (sdk/GET (get-client) path))

(defn get-endpoints []
  (GET "/endpoints"))
