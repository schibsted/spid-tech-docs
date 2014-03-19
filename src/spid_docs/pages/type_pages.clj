(ns spid-docs.pages.type-pages
  (:require [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [type-path]]))

(defn render-type-definition
  "Render the type definition specified by the type map. See the validation data
   structures for a description of the contents. local-types is a list of type
   ids for types that are expected to be found inline on the page. Thus, they
   will be linked as anchors instead of to external pages."
  [type local-types]
  [:table])

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
