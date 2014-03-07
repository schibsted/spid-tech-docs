(ns spid-docs.examples
  "Functions to facilitate extracting code examples from the example repo."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.homeless :refer [chop-off-common-whitespace]]))

(def examples-dir
  "Examples directory, relative to resources/"
  "example-repo/examples/")

(defn- find-example
  "Finds a code example in the provided code with the given delimiters."
  [start-delim end-delim code]
  (->> (str/split code #"\n")
       (drop-while #(not= start-delim (str/trim %)))
       (drop 1)
       (take-while #(not= end-delim (str/trim %)))
       (chop-off-common-whitespace)
       (str/join "\n")))

(defn- strip-example-dir
  "Returns the relative path to a file inside the example directory"
  [path]
  (->> (str/split path #"/")
       (drop 2)
       (str/join "/")))

(defn- with-missing-example-warning
  "Makes sure a warning is thrown when trying to refer a missing code example."
  [path title example]
  (if (seq example)
    example
    (throw (Exception. (format "No example '%s' found in %s" title path)))))

(defmulti create-example
  "Render code example. Will throw an exception if trying to render a code
   example that does not exist"
  (fn [lang path title code] lang))

(defmethod create-example :php [_ path title code]
  (str "<?php // " (strip-example-dir path) "\n"
       (with-missing-example-warning path title
         (find-example (format "/** %s */" title) "/**/" code))))

(defmethod create-example :java [_ path title code]
  (with-missing-example-warning path title
    (find-example (format "/** %s */" title) "/**/" code)))

(defmethod create-example :clj [_ path title code]
  (with-missing-example-warning path title
    (find-example (format ";;; %s" title) ";;;" code)))

(defn- read-example-file [lang path]
  (slurp (io/resource (str examples-dir (name lang) path))))

(defn read-example [lang title path]
  (create-example lang path title
                  (read-example-file lang path)))
