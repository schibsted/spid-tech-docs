(ns spid-docs.seq-diagrams
  (:require [clojure.java.shell :refer [sh]]))

(defn generate-svg [definition]
  (let [result (sh "node" "index.js"
                   :dir "seq-diagrams/"
                   :in definition)]
    (if (= 0 (:exit result))
      (:out result)
      (throw (ex-info result)))))
