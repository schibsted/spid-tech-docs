(ns spid-docs.cultivate.apis)

(defn belongs-to [service-id api-id]
  (fn [endpoint]
    (->> endpoint
         :categorization
         (some #(= [service-id api-id] %)))))

(defn cultivate-service-api [api service-id endpoints]
  (assoc api :endpoints (filter (belongs-to service-id (:id api)) endpoints)))

(defn cultivate-service [service endpoints]
  (assoc service :apis
         (map #(cultivate-service-api % (:id service) endpoints) (:apis service))))

(defn cultivate-apis [apis endpoints]
  (map #(cultivate-service % endpoints) apis))

(defn endpoint-count [api]
  (->> (:apis api)
       (mapcat :endpoints)
       count))

(defn- by-endpoint-count [a b]
  (> (endpoint-count a) (endpoint-count b)))

(defn- shortest-index [col count-column]
  (loop [idx -1
         shortest (inc (apply max (map count-column col)))
         i 0]
    (if (< i (count col))
      (if (< (count-column (nth col i)) shortest)
        (recur i (count-column (nth col i)) (inc i))
        (recur idx shortest (inc i)))
      idx)))

(defn- endpoints-in-column [col]
  (reduce + (map endpoint-count col)))

(defn columnize [apis cols]
  (loop [columns (mapv (fn [_] []) (range cols))
         sorted-apis (sort by-endpoint-count apis)]
    (if (seq sorted-apis)
      (recur (update-in columns [(shortest-index columns endpoints-in-column)] #(conj % (first sorted-apis)))
             (rest sorted-apis))
      columns)))
