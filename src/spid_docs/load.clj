(ns spid-docs.load
  "Various utilities used across many namespaces"
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- check-resource-existance [file]
  (if (nil? (io/resource file))
    (throw (Exception. (str "Unable to load " file ", no such file resources/" file)))))

(defn load-edn-str [content & [file-name]]
  (let [source (or file-name "EDN content")
        forms (try
                (read-string (str "[" (str/trim content) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " source ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str source " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

(defn load-edn
  "Read an edn file from resources. The path should be relative to the resources
  directory, e.g. (load-edn 'config.edn') to read resources/config.edn"
  [file]
  (check-resource-existance file)
  (load-edn-str (slurp (clojure.java.io/resource file)) file))

(defn load-json
  "Read a json file from resources. Keys are keywordized."
  [file]
  (check-resource-existance file)
  (json/read-str (slurp (io/resource file)) :key-fn keyword))
