(ns spid-docs.seq-diagrams
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [digest :refer [md5]]))

(defn generate-svg [definition]
  (let [result (sh "node" "index.js"
                   :dir "seq-diagrams/"
                   :in definition)]
    (if (= 0 (:exit result))
      (:out result)
      (throw (ex-info "Could not generate svg" result)))))

(defn get-sequence-diagram [definition]
  (let [path (str "sequence-diagrams/" (md5 definition) ".svg")]
    {:path (str "/" path)
     :svg (if-let [cached (io/resource path)]
            (slurp cached)
            (let [svg (generate-svg definition)]
              (spit (str "generated/" path) svg)
              svg))}))
