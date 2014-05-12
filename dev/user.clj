(ns user
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint pp print-table]]
            [clojure.repl :refer :all]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.tools.namespace.repl :refer [refresh]]
            [hiccup.core :as hiccup]
            [spid-docs.content :as content]
            [spid-docs.validate-raw :refer [validate-raw-content]]))

(comment
  ;; This is very frequently very useful for REPL stuff
  (def endpoints (:endpoints (-> (content/load-content)
                                 validate-raw-content
                                 content/cultivate-content))))
