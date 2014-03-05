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
