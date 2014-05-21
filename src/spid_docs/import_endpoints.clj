(ns spid-docs.import-endpoints
  (:require [clojure.data.json :as json]
            [clojure.pprint :refer [pprint]]
            [spid-docs.api-client :refer [GET get-config get-server-token config-exists?]]
            [spid-docs.content :refer [load-content]]
            [spid-docs.diff-endpoints :refer [diff-endpoints]]))

(def import-path "/endpoints")

(defn- report-changed-endpoint-keys [diff]
  (when (seq (:schema diff))
    (println "Note! The endpoint fields have changed since last import:")
    (doseq [field (-> diff :schema :added)] (println (format "  - Added field \"%s\"" field)))
    (doseq [field (-> diff :schema :removed)] (println (format "  - Removed field \"%s\"" field)))
    (println)))

(defn- report-endpoint-changes [{:keys [changed added removed]}]
  (when changed
    (println "Changed endpoints:")
    (doseq [e changed] (println "  -" (name (:method e)) (:path e)))
    (println))
  (when added
    (println "Added endpoints:")
    (doseq [e added] (println "  -" (name (:method e)) (:path e)))
    (println))
  (when removed
    (println "Removed endpoints:")
    (doseq [e removed] (println "  -" (name (:method e)) (:path e)))
    (println)))

(defn- report-example-params-changes [diff example-params]
  (when (:params diff)
    (let [missing (remove example-params (-> diff :params :added))
          superflous (filter example-params (-> diff :params :removed))]
      (when (seq missing)
        (println "NB! Examples are missing for these new parameters,")
        (println "and must be added to resources/example-params.edn:")
        (doseq [p missing] (println "  -" p))
        (println))
      (when (seq superflous)
        (println "Consider removing these outdated parameters from resources/example-params.edn:")
        (doseq [p superflous] (println "  -" p))
        (println)))))

(defn- load-new-endpoints []
  (println (str "Fetching " (:spid-base-url (get-config)) "/api/2" import-path))
  (let [response (GET (get-server-token) import-path)]
    (when-not (= 200 (:status response))
      (throw (Exception.
              (format "Unexpected status %d when fetching endpoints at %s"
                      (:status response) import-path))))
    (:data response)))

(defn import-endpoints []
  (if-not (config-exists?)
    (do
      (println "Aborting import, no configuration file detected.")
      (println)
      (println "  cp resources/config.sample.edn resources/config.edn")
      (println "  vim resources/config.edn")
      (println))
    (let [old-content (load-content)
          old-endpoints (:endpoints old-content)
          new-endpoints (load-new-endpoints)
          diff (diff-endpoints old-endpoints new-endpoints)]
      (if-not diff
        (println "No changes detected.")
        (if (:breaking-change? diff)
          (do
            (report-changed-endpoint-keys diff)
            (println "Aborting import, since schema has changed. It no longer passes")
            (println "our expectations for the data structure.")
            (println)
            (println "If this is just a new field to be ignored by the tech-docs")
            (println "site, add it as an optional field to the schema in validate_raw.clj.")
            (println "Otherwise, there's Clojure programming ahead."))
          (do
            (report-endpoint-changes diff)
            (report-example-params-changes diff (:example-params old-content))
            (report-changed-endpoint-keys diff)
            (spit "generated/cached-endpoints.edn" (with-out-str (pprint new-endpoints)))
            (println "Wrote generated/cached-endpoints.edn")))))))
