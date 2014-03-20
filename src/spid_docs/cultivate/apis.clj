(ns spid-docs.cultivate.apis)

(defn categorize-endpoints [apis endpoints]
  (reduce #(assoc-in %1 [(first %2) :endpoints] (second %2))
          apis
          (group-by (comp vals :category) endpoints)))

(defn cultivate-apis [apis endpoints]
  (categorize-endpoints apis endpoints))
