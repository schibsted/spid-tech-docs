(ns spid-docs.pages.type-pages
  (:require [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [type-path]]))

(defn- render-availability [field]
  (if (:always-available? field) [:span.check "✓"]))

(defn- link-to-type
  "Link to the type passed in. The type should be either a keyword, which names
   the type, or it may be a vector. If it is a vector, it should contain a
   single keyword, and denotes a list of the type named by the keyword.

   The types map defines possible types to link to. If the type is a link to a
   type with a description, a link will be generated. If the target type has no
   description, it is considered trivial, and no link is offered, only the type
   name. If the target type is pertinent, it is assumed rendered on the same
   page, and an anchor link is generated."
  [type types]
  (let [list-type? (vector? type)
        type-id (if list-type? (first type) type)
        type-name (name type-id)
        prefix (if list-type? "list of " "")
        postfix (if list-type? (if (.endsWith type-name "s") "es" "s") "")
        typedef (type-id types)
        path (if (:pertinent? typedef)
               (str "#" (name type))
               (type-path typedef))]
    (list prefix
          (if path
            [:a {:href path} type-name]
            type-name)
          postfix)))

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
