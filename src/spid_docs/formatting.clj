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

(defn to-id-str [str]
  "Replaces all special characters with dashes, avoiding leading,
   trailing and double dashes."
  (-> (.toLowerCase str)
      (str/replace #"[^a-zA-Z]+" "-")
      (str/replace #"-$" "")
      (str/replace #"^-" "")))

(defn- shortest-index
  "Given a collection, find the index where the provided count-fn function
  returns the lowest number of items. Supporting function for columnize."
  [coll count-fn]
  (loop [idx -1
         shortest (inc (apply max (map count-fn coll)))
         i 0]
    (if (< i (count coll))
      (if (< (count-fn (nth coll i)) shortest)
        (recur i (count-fn (nth coll i)) (inc i))
        (recur idx shortest (inc i)))
      idx)))

(defn- column-count
  "Using a specific counting function, decide how many items are present in a
  column. Supporting function for columnize."
  [count-fn]
  (fn [col]
    (reduce + (map count-fn col))))

(defn columnize
  "Breaks a list of data into +cols+ roughly equally sized columns. With only
   two arguments, columnize takes a list of lists, and breaks it up in n columns
   of lists, trying to balance not the number of lists in each column, but the
   number of items in each column. Example:

   (columnize [[1 2 3] [4 5] [6] [7 8] [9 10 11]] 3)
   ;=> [[[1 2 3] [6]] [[9 10 11]] [[4 5] [7 8]]]

   By providing the count-fn argument, you can decide how the item count for
   individual items are calculated, and can thus columnize any kind of
   data (perhaps deeply nested)"
  [coll cols & [count-fn]]
  (let [count-fn (or count-fn count)]
    (loop [columns (mapv (fn [_] []) (range cols))
           sorted-coll (sort #(> (count-fn %1) (count-fn %2)) coll)]
      (if (seq sorted-coll)
        (recur (update-in columns [(shortest-index columns (column-count count-fn))]
                          #(conj % (first sorted-coll)))
               (rest sorted-coll))
        columns))))
