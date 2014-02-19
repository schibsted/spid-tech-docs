(ns spid-docs.core
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [spid-sdk-clojure.core :refer [create-client GET]]))

(defn load-edn [file]
  (let [content (slurp (clojure.java.io/resource file))
        forms (try
                (read-string (str "[" (str/trim content) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(defn get-endpoints-from-api []
  (let [cred (load-edn "credentials.edn")
        client (create-client (:client-id cred) (:client-secret cred))]
    (GET client "/endpoints")))

(defn get-endpoints-from-disk []
  (load-edn "cached-endpoints.edn"))

(def get-endpoints get-endpoints-from-disk)
