(ns spid-docs.concepts
  (:require [fs.core :as fs]
            [spid-docs.enlive :as enlive]
            [spid-docs.formatting :as formatting]))

(defn concept-path [concept]
  (str "/concepts/" (name concept)))

(defn filename-to-path [filename]
  (-> filename
      fs/base-name
      fs/split-ext
      first
      concept-path))

(defn- create-concept [concept]
  (let [body (formatting/to-html concept)]
    {:title (->> body (enlive/parse) (enlive/select [:h1]) first :content (apply str))
     :body body}))

(defn create-pages [concepts]
  (->> concepts
       (map (juxt #(filename-to-path (first %)) #(partial create-concept (second %))))
       (into {})))
