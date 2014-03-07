(ns spid-docs.examples
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.homeless :refer [min* subs* chop-off-common-whitespace]]))

(def examples-dir "example-repo/examples/")

(defn- find-example [start-delim end-delim code]
  (->> (str/split code #"\n")
       (drop-while #(not= start-delim (str/trim %)))
       (drop 1)
       (take-while #(not= end-delim (str/trim %)))
       (chop-off-common-whitespace)
       (str/join "\n")))

(defn- strip-example-dir [path]
  (->> (str/split path #"/")
       (drop 2)
       (str/join "/")))

(defn- with-missing-example-warning [path title example]
  (if (seq example)
    example
    (throw (Exception. (format "No example '%s' found in %s" title path)))))

(defmulti create-example (fn [lang path title code] lang))

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
