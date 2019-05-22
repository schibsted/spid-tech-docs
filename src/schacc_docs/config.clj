(ns spid-docs.config
    (:require [clojure.java.io :as io]
              [spid-docs.load :refer [load-edn]]))

(defn config-exists? []
(io/resource "config.edn"))

(def get-config (memoize #(load-edn "config.edn")))