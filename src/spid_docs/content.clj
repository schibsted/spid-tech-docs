(ns spid-docs.content
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.core :as spid]
            [spid-sdk-clojure.core :refer [create-client GET]]
            [stasis.core :as stasis]))

(defn load-edn [file]
  (let [content (slurp (clojure.java.io/resource file))
        forms (try
                (read-string (str "[" (str/trim content) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(defn- get-endpoints-from-api []
  (let [cred (load-edn "credentials.edn")
        client (create-client (:client-id cred) (:client-secret cred))]
    (GET client "/endpoints")))

(defn- get-endpoints-from-disk []
  (load-edn "cached-endpoints.edn"))

(def get-endpoints get-endpoints-from-disk)

(defn endpoint-path-to-filename [path]
  ;; The error endpoint is not really an endpoint, and is the only one
  ;; with an asterisk. Special case it.
  (if (= "api/2/*" path)
    "not-found.edn"
    (-> path
        (str/replace #"[/{}]+" "-")
        (str/replace #"(^-)|(-$)" "")
        (str ".edn"))))

(defn cultivate-endpoint [endpoint]
  (let [endpoint-resource (-> endpoint :path endpoint-path-to-filename)]
    (if (->> endpoint-resource
             (str "resources/endpoints/")
             (io/as-file)
             .exists)
      (merge (load-edn (str "endpoints/" endpoint-resource)) endpoint)
      endpoint)))

(defn load-content []
  {:endpoints (get-endpoints)
   :articles (stasis/slurp-directory "resources/articles" #"\.md$")
   :concepts (stasis/slurp-directory "resources/concepts" #"\.md$")
   :types (load-edn "types.edn")})
