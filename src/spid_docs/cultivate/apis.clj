(ns spid-docs.cultivate.apis
  "Process data in resources/apis.edn into a map of endpoints, where the
  two-level category is key and a map with description and related endpoints
  are the values.")

(defn categorize-endpoints [apis endpoints]
  (reduce #(assoc-in %1 [(first %2) :endpoints] (second %2))
          apis
          (group-by (comp vals :category) endpoints)))

(defn internalize-keys [apis]
  (zipmap (keys apis)
          (map #(assoc (apis %)
                  :category (first %)
                  :api (second %)) (keys apis))))

(defn cultivate-apis
  "Sort the endpoints under their respective API categories."
  [apis endpoints]
  (-> (categorize-endpoints apis endpoints)
      internalize-keys))
