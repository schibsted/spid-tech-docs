(ns spid-docs.cultivate.endpoints
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.core :as spid]))

(defn endpoint-path-to-filename [path]
  ;; The error endpoint is not really an endpoint, and is the only one
  ;; with an asterisk. Special case it.
  (if (= "api/2/*" path)
    "not-found.edn"
    (-> path
        (str/replace #"[/{}]+" "-")
        (str/replace #"(^-)|(-$)" "")
        (str ".edn"))))

(defn maybe-load-file [file]
  (let [filename (str "resources/" file)]
    (if (->> filename (io/as-file) .exists)
      (spid/load-edn file) {})))

(defn cultivate-endpoint [endpoint]
  (let [endpoint-resource (-> endpoint :path endpoint-path-to-filename)]
    (merge endpoint
           (maybe-load-file (str "endpoints/" endpoint-resource))
           (maybe-load-file (str "sample-responses/" endpoint-resource)))))
