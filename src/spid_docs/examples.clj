(ns spid-docs.examples
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def examples-dir "example-repo/examples/")

(defn- min*
  "Like min, but takes a list - and 0 elements is okay."
  [vals]
  (when (seq vals) (apply min vals)))

(defn- subs*
  "Like subs, but safe - ie, doesn't barf on too short."
  [s len]
  (if (> (count s) len)
    (subs s len)
    s))

(defn- fewest-preceding-spaces [lines]
  (->> lines
       (remove #(empty? %))
       (map #(count (re-find #"^ +" %)))
       (min*)))

(defn- chop-off-common-whitespace [lines]
  (let [superflous-spaces (fewest-preceding-spaces lines)]
    (map #(subs* % superflous-spaces) lines)))

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

(defn- warn-missing-example [path title example]
  (if (seq example)
    example
    (throw (Exception. (format "No example '%s' found in %s" title path)))))

(defmulti create-example (fn [lang path title code] lang))

(defmethod create-example :php [_ path title code]
  (str "<?php // " (strip-example-dir path) "\n"
       (warn-missing-example path title
                           (find-example (format "/** %s */" title) "/**/" code))))

(defmethod create-example :java [_ path title code]
  (warn-missing-example path title
                        (find-example (format "/** %s */" title) "/**/" code)))

(defn read-example [lang title path]
  (create-example lang path title (slurp (io/resource (str examples-dir (name lang) path)))))
