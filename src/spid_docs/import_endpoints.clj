(ns spid-docs.import-endpoints
  (:require [clojure.data.json :as json]
            [clojure.set :as set]
            [schema.core :refer [validate Str]]
            [spid-docs.api-client :refer [GET get-config get-server-token]]
            [spid-docs.content :refer [load-content]]
            [spid-docs.validate-raw :refer [Endpoint]]))

(def import-path "/endpoints")

(defn- assert-200-ok [status]
  (when-not (= 200 status)
    (throw (Exception.
            (format "Unexpected status %d when fetching endpoints at %s"
                    status import-path)))))

(defn- extract-relevant-keys [endpoints]
  (->> (concat (map name (mapcat keys endpoints))
               (map #(str "httpMethods/" (name %))
                    (mapcat keys (mapcat (comp vals :httpMethods) endpoints))))
       set))

(defn- report-changed-endpoint-keys [old new]
  (let [old-keys (extract-relevant-keys old)
        new-keys (extract-relevant-keys new)
        added (sort (set/difference new-keys old-keys))
        removed (sort (set/difference old-keys new-keys))]
    (when (or (seq added) (seq removed))
      (println "Note! The endpoint fields have changed since last import:")
      (doseq [field added] (println (format "  - Added field \"%s\"" field)))
      (doseq [field removed] (println (format "  - Removed field \"%s\"" field)))
      (println))))

(defn- schema-is-valid? [endpoints]
  (try
    (validate [Endpoint] endpoints)
    true
    (catch Exception e
      false)))

(defn- report-endpoint-changes [old new]
  (let [old-paths (set (map :path old))
        new-paths (set (map :path new))
        added (sort (set/difference new-paths old-paths))
        removed (sort (set/difference old-paths new-paths))
        changed (set/difference (set (map :path (set/difference (set old) (set new))))
                                added removed)]
    (when (seq changed)
      (println "Changed endpoints:")
      (doseq [path changed] (println "  -" path))
      (println))
    (when (seq added)
      (println "Added endpoints:")
      (doseq [path added] (println "  -" path))
      (println))
    (when (seq removed)
      (println "Removed endpoints:")
      (doseq [path removed] (println "  -" path))
      (println))

    ;; return: did anything change?
    (or (seq changed)
        (seq added)
        (seq removed))))

(defn import-endpoints []
  (println (str "Importing endpoints from " (:spid-base-url (get-config)) "/api/2" import-path))
  (println (apply str (repeat 80 "-")))
  (let [old-endpoints (:endpoints (load-content))
        response (GET (get-server-token) import-path)
        _ (assert-200-ok (:status response))
        new-endpoints (:data response)]
    (if-not (schema-is-valid? new-endpoints)
      (do (report-changed-endpoint-keys old-endpoints new-endpoints)
          (println "Aborting import, since schema has changed. It no longer passes")
          (println "our expectations for the data structure.")
          (println)
          (println "If this is just a new field to be ignored by the tech-docs")
          (println "site, add it as an optional field to the schema in validate_raw.clj.")
          (println "Otherwise, there's Clojure programming ahead."))
      (do
        (if-not (report-endpoint-changes old-endpoints new-endpoints)
          (println "No changes detected.")
          (do
            (report-changed-endpoint-keys old-endpoints new-endpoints)
            (spit "generated/cached-endpoints.json"
                  (:body response))
            (println "Wrote generated/cached-endpoints.json")))))))
