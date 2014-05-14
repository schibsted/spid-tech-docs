(ns spid-docs.json
  (:import [com.google.gson GsonBuilder]))

(defn format-json [data]
  (-> (GsonBuilder.)
      .setPrettyPrinting
      .create
      (.toJson data)))
