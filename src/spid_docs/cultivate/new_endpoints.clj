(ns spid-docs.cultivate.new-endpoints
  (:require [clojure.string :as str]))

(defn- generate-id [path]
  (-> path
      (str/replace #"[^a-zA-Z]+" "-")
      (str/replace #"-$" "")))

(def ^:private verbs {:GET "Get"
                      :POST "Create"
                      :DELETE "Delete"})

(defn- fix-multimethod-name [name method]
  (str/replace name #"(?i)^(?:(?:get|create|delete),? )+or (?:get|create|delete) " (str (verbs method) " ")))

(defn- cultivate-endpoint-1 [endpoint [method details]]
  {:id (-> (:path endpoint) generate-id)
   :path (str "/" (:path endpoint))
   :method method
   :name (fix-multimethod-name (:name endpoint) method)})

(defn cultivate-endpoint [endpoint]
  (map (partial cultivate-endpoint-1 endpoint) (:httpMethods endpoint)))
