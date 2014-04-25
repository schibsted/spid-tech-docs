(ns spid-docs.api-client
  "Functions for working with the SPiD API. The SPiD API is used by the
   documentation to get sample responses etc."
  (:require [spid-docs.core :as spid]
            [spid-sdk-clojure.core :as sdk]
            [spid-sdk-clojure.raw :as api]))

(def get-config (memoize #(spid/load-edn "config.edn")))

(def get-client (memoize #(let [conf (get-config)]
                            (sdk/create-server-client (:client-id conf) (:client-secret conf) {:spp-base-url (:spid-base-url conf), :redirect-uri "http://localhost"}))))

(defn GET [path]
  (sdk/GET (get-client) path))

(defn raw-GET [path & [params]]
  (api/GET (get-client) path params))

(defn raw-POST [path params]
  (api/POST (get-client) path params))

(defn raw-DELETE [path params]
  (api/DELETE (get-client) path params))
