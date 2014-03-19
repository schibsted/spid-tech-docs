(ns spid-docs.pages.type-pages
  (:require [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [type-path]]))

(defn- render-availability [field]
  (if (:always-available? field) [:span.check "✓"]))

(defn- link-to-type [type types]
  (let [typedef (type types)
        path (if (:pertinent? typedef)
               (str "#" (name type))
               (type-path typedef))]
    (if path
      [:a {:href path} type]
      type)))

(defn- render-type-field [field types]
  [:tr
   [:th [:h4.nowrap (str (:name field) (render-availability field))]]
   [:td
    [:h5 (link-to-type (:type field) types)]
    [:p.faded (markdown/render-inline (:description field))]]])

(defn render-type-definition
  "Render the type definition specified by the type map. See the validation data
   structures for a description of the contents. local-types is a list of type
   ids for types that are expected to be found inline on the page. Thus, they
   will be linked as anchors instead of to external pages."
  [type types]
  [:h3 (:name type)]
  [:a {:name (name (:id type))}]
  (markdown/render (:description type))
  [:table.sectioned.mbl
   (map #(render-type-field % types) (:fields type))])

(defn create-page [type]
  {:body [:div.wrap
          [:h1 (name (:id type))]
          (markdown/render (:description type))]})

(defn create-pages
  "Takes a map of types (typically those defined in resources/types.edn) and
   returns a map of url => page function."
  [types]
  (->> (vals types)
       (filter :description)
       (map (juxt type-path #(partial create-page %)))
       (into {})))
