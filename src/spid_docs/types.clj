(ns spid-docs.types
  (:require [spid-docs.formatting :refer [to-html]]))

(defn- create-page [type]
  {:body (list [:h1 (name (:id type))]
               (to-html (:description type)))})

(defn type-path [type]
  (if (:description type)
    (str "/types/" (name (:id type)))))

(defn create-pages [types]
  (->> types
       (filter :description)
       (map (juxt type-path #(partial create-page %)))
       (into {})))
