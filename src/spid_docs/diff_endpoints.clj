(ns spid-docs.diff-endpoints
  (:require [clojure.set :as set]
            [schema.core :as schema]
            [spid-docs.homeless :refer [assoc-if assoc-non-nil]]
            [spid-docs.validate-raw :refer [Endpoint]]))

(defn- schema-is-valid? [endpoints]
  "Check if the given endpoints matches our expectations."
  (not (schema/check [Endpoint] endpoints)))

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

(defn- add-changes [diff old new]
  "Find all changed endpoints, remove the ones that have been added and removed
   since they're included elsewhere, and add to the diff if any."
  (let [changes (-> (set (mapcat get-signatures
                                 (set/difference (set (mapcat flatten-http-methods old))
                                                 (set (mapcat flatten-http-methods new)))))
                    (set/difference (:added diff) (:removed diff)))]
    (if (seq changes)
      (assoc diff :changed changes)
      diff)))

(defn- all-params [endpoint]
  "Return a list of all params for the endpoint."
  (concat (:pathParameters endpoint)
          (mapcat :optional (vals (:httpMethods endpoint)))
          (mapcat :required (vals (:httpMethods endpoint)))))

(defn- all-types [endpoint]
  "Return a list of all return types for the endpoint."
  (map :type (mapcat :responses (vals (:httpMethods endpoint)))))

(defn- all-relevant-properties [endpoint]
  "Return a list of properties used to describe the endpoint. This includes the
   properties in httpMethods."
  (concat (map name (keys endpoint))
          (map #(str "httpMethods/" (name %))
               (mapcat keys (vals (:httpMethods endpoint))))))

(defn- diff-vals [old new get-vals]
  "Calls get-vals on each endpoint in old and new, compares the lists and
   returns a map of added and removed values."
  (let [old-vals (set (mapcat get-vals old))
        new-vals (set (mapcat get-vals new))
        added (set/difference new-vals old-vals)
        removed (set/difference old-vals new-vals)]
    (when (or (seq added) (seq removed))
      (-> {}
          (assoc-non-nil :added added)
          (assoc-non-nil :removed removed)))))

(defn diff-endpoints [old new]
  "Diff old and new endpoints, and return a summary of the changes."
  (when-not (= old new)
    (-> (diff-vals old new get-signatures)
        (add-changes old new)
        (assoc-if (not (schema-is-valid? new)) :breaking-change? true)
        (assoc-non-nil :schema (diff-vals old new all-relevant-properties))
        (assoc-non-nil :params (diff-vals old new all-params))
        (assoc-non-nil :types (diff-vals old new all-types)))))
