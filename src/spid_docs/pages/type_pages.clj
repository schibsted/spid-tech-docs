(ns spid-docs.pages.type-pages
  (:require [spid-docs.pimp.markdown :as markdown]))

(defn type-path
  "Given a type (typically one entry from the vector in resources/types.end),
   return its URL"
  [type]
  (if (:description type)
    (str "/types/" (name (:id type)))))

(defn create-page [type]
  {:body [:div.wrap
          [:h1 (name (:id type))]
          (markdown/render (:description type))]})

(defn create-pages
  "Takes a list of types (typically those defined in resources/types.edn) and
   returns a map of url => page function."
  [types]
  (->> types
       (filter :description)
       (map (juxt type-path #(partial create-page %)))
       (into {})))
