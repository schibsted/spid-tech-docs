(ns spid-docs.seq-diagrams
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [digest :refer [md5]]))

(def npm-install
  "Install npm dependencies"
  (delay
    (let [result (sh "npm" "install"
                     :dir "seq-diagrams/")]
      (if (= 0 (:exit result))
        (:out result)
        (throw (ex-info "Could not npm install" result))))))

(defn generate-svg
  "Shell out to the sequence diagram generator in node, passing the
   definition through standard in."
  [definition]
  (let [result (sh "node" "index.js"
                   :dir "seq-diagrams/"
                   :in definition)]
    (if (= 0 (:exit result))
      (:out result)
      (throw (ex-info "Could not generate svg" result)))))

(defn get-sequence-diagram [definition]
  "Hash the definition to generate a unique path, and either return the
   previously generated svg, or generate it now, save and return."
   @npm-install
  (let [path (str "sequence-diagrams/" (md5 definition) ".svg")]
    {:path (str "/" path)
     :svg (if-let [cached (io/resource path)]
            (slurp cached)
            (let [svg (generate-svg definition)]
              (spit (str "generated/" path) svg)
              svg))}))
