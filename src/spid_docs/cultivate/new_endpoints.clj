(ns spid-docs.cultivate.new-endpoints
  (:require [clojure.string :as str]))

(defn- generate-id [path]
  (-> path
      (str/replace #"[^a-zA-Z]+" "-")
      (str/replace #"-$" "")))

(defn- cultivate-endpoint-1 [endpoint method]
  [{:id (-> endpoint :path generate-id)}])

(defn cultivate-endpoint [endpoint]
  (mapcat (partial cultivate-endpoint-1 endpoint) (:httpMethods endpoint)))
