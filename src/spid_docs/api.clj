(ns spid-docs.api
  (:require [spid-docs.core :as spid]
            [spid-sdk-clojure.core :as sdk]))

(def config (memoize #(spid/load-edn "config.edn")))

(def get-client (memoize #(let [conf (config)]
                            (sdk/create-client (:client-id conf) (:client-secret conf)))))

(defn GET [path]
  (sdk/GET (get-client) path))

(defn get-endpoints []
  (GET "/endpoints"))
