(ns spid-docs.json
  (:import [com.google.gson GsonBuilder JsonParser])
  (:require [clojure.data.json :as json]))

(defn to-json [data]
  (json/json-str data :escape-slash false))

(defn format-json [data]
  (.toJson (-> (GsonBuilder.)
               .setPrettyPrinting
               .serializeNulls
               .create)
           (.parse (JsonParser.) (to-json data))))

;; First turning data to JSON, then parsing it out again, just to turn it back
;; to pretty printed json, what what WHAT?
;;
;; Here's why:
;;
;; - clojure.data.json creates Clojure-style pretty-printing for the JSON, which
;;   is entirely non-idiomatic for JavaScript
;;
;; - GSON doesn't understand the keywords in our Clojure maps
;;
;; - and finally, clojure.walk/stringify-keys doesn't preserve ordering
;;
;; So it all makes perfect sense, you see. Perfect sense.
