(ns spid-docs.core
  "Various utilities used across many namespaces"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn load-edn
  "Read an edn file from resources. The path should be relative to the resources
  directory, e.g. (load-end 'config.edn') to read resources/config.edn"
  [file]
  (if (nil? (io/resource file))
    (throw (Exception. (str "Unable to load " file ", no such file resources/" file))))
  (let [content (slurp (clojure.java.io/resource file))
        forms (try
                (read-string (str "[" (str/trim content) "]"))
                (catch Exception e
                  (throw (Exception. (str "Error in " file ": " (.getMessage e))))))]
    (when (> (count forms) 1)
      (throw (Exception. (str "File " file " should contain only a single map, but had " (count forms) " forms."))))
    (first forms)))

