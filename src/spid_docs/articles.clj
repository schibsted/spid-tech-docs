(ns spid-docs.articles
  (:require [clojure.string :as str]
            [spid-docs.confluence :as confluence]
            [spid-docs.formatting :refer [to-html]]))

(defn- to-page-url [[path _]]
  (str/replace path #"\.md$" "/"))

(defn- create-post [[_ markdown]]
  {:title ""
   :body (to-html markdown)})

(defn create-pages [articles]
  (->> articles
       (map (juxt to-page-url #(partial create-post %)))
       (into {})))

(defn- to-export-url [[path _]]
  (str/replace (str "/confluence" path) #"\.md$" ".txt"))

(defn- create-export [[_ markdown]]
  (-> markdown to-html confluence/to-storage-format))

(defn create-confluence-exports [articles]
  (->> articles
       (map (juxt to-export-url create-export))
       (into {})))
