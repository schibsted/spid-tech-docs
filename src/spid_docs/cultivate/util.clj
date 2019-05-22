(ns spid-docs.cultivate.util
  (:require [clojure.string :as str]))

(defn- parse-relevant-endpoints-line [l]
  "Takes a string like: GET /the/path and makes a map out of it."
  (let [[method path] (str/split l #" " 2)]
    {:method (keyword method)
     :path path}))

(defn parse-relevant-endpoints [relevant-endpoints]
  "Takes a list of newline separated endpoint signatures, and turns it
   into a map."
  (when relevant-endpoints
   (->> relevant-endpoints
        str/trim
        str/split-lines
        (map parse-relevant-endpoints-line))))
