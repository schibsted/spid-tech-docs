(ns spid-docs.diff-endpoints
  (:require [clojure.set :as set]
            [schema.core :as schema]
            [spid-docs.homeless :refer [assoc-if assoc-non-nil]]
            [spid-docs.validate-raw :refer [Endpoint]]))

(defn- extract-relevant-keys [endpoints]
  "Return a list of all property names used for all endpoints, including the
   properties used in all httpMethod objects."
  (->> (concat (map name (mapcat keys endpoints))
               (map #(str "httpMethods/" (name %))
                    (mapcat keys (mapcat (comp vals :httpMethods) endpoints))))
       set))

(defn- schema-is-valid? [endpoints]
  "Check if the given endpoints matches our expectations."
  (not (schema/check [Endpoint] endpoints)))

(defn- find-schema-changes [old new]
  "Compare properties used to describe endpoints in old and new version."
  (let [old-keys (extract-relevant-keys old)
        new-keys (extract-relevant-keys new)
        added (set/difference new-keys old-keys)
        removed (set/difference old-keys new-keys)]
    (when (or (seq added) (seq removed))
      {:added added, :removed removed})))

(defn- get-signatures [endpoint]
  "Return a list of {:path :method} for each of httpMethods in endpoint."
  (map (fn [method] {:path (:path endpoint)
                     :method method})
       (keys (:httpMethods endpoint))))

(defn- flatten-http-methods [endpoint]
  "Return a list of endpoints each with a single entry in httpMethods from the original."
  (let [methods (:httpMethods endpoint)]
    (map (fn [method] (assoc endpoint :httpMethods (select-keys methods [method])))
         (keys methods))))

(defn- find-endpoint-changes [old new]
  "Return a map of added, removed and changed endpoints."
  (let [old-signatures (set (mapcat get-signatures old))
        new-signatures (set (mapcat get-signatures new))
        added (set/difference new-signatures old-signatures)
        removed (set/difference old-signatures new-signatures)
        changed (-> (set (mapcat get-signatures
                                 (set/difference (set (mapcat flatten-http-methods old))
                                                 (set (mapcat flatten-http-methods new)))))
                    (set/difference added removed))]
    (cond-> {}
            (seq added) (assoc :added added)
            (seq removed) (assoc :removed removed)
            (seq changed) (assoc :changed changed))))

(defn- all-params [endpoint]
  (concat (:pathParameters endpoint)
          (mapcat :optional (vals (:httpMethods endpoint)))
          (mapcat :required (vals (:httpMethods endpoint)))))

(defn- find-param-changes [old new]
  "Return a map of added and removed parameter names."
  (let [old-params (set (mapcat all-params old))
        new-params (set (mapcat all-params new))
        added (set/difference new-params old-params)
        removed (set/difference old-params new-params)]
    (when (or (seq added) (seq removed))
      (-> {}
          (assoc-non-nil :added added)
          (assoc-non-nil :removed removed)))))

(defn- add-schema-changes [m old new]
  "If there are any schema changes, add them to the map along with :schema-change set to true."
  (if-let [schema-changes (find-schema-changes old new)]
    (assoc m
      :schema-change? true
      :schema schema-changes)
    m))

(defn diff-endpoints [old new]
  "Diff old and new endpoints, and return a summary of the changes."
  (when-not (= old new)
    (-> {}
        (add-schema-changes old new)
        (assoc-if (not (schema-is-valid? new)) :breaking-change? true)
        (assoc-non-nil :params (find-param-changes old new))
        (merge (find-endpoint-changes old new)))))
