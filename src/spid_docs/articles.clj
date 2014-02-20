(ns spid-docs.articles
  (:require [clojure.string :as str]
            [spid-docs.formatting :refer [to-html]]
            [spid-docs.highlight :refer [highlight-code-blocks]]))

(defn- create-url [[path _]]
  (str/replace path #"\.md$" "/"))

(defn- create-post [[_ markdown]]
  {:title ""
   :body (->> markdown to-html highlight-code-blocks)})

(defn create-pages [articles]
  (->> articles
       (map (juxt create-url #(partial create-post %)))
       (into {})))
