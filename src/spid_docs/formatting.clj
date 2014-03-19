(ns spid-docs.formatting
  (:require [clojure.string :as str]))

(defn pluralize
  "Add 's' to the end of a word to pluralize it. If it already ends in 's', add
  'es'. If the optional count parameter is provided, the word will be pluralized
  unless the count is 1, as in '1 apple, 2 apples'."
  [word & [count]]
  (if (= count 1) word (str word (if (.endsWith word "s") "es" "s"))))

(defn enumerate-humanely
  "Take a list of words, and return a formatted string that enumerates the words
   in a human way, e.g. 'cow, horse and pig'."
  [coll]
  (str/join "" (drop
                1 (interleave
                   (into
                    (list (str (if (= (count coll) 2) " and " ", and ")) "")
                    (repeat (dec (count coll)) ", "))
                   coll))))
