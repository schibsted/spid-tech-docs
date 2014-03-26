(ns spid-docs.pages.type-pages
  (:require [spid-docs.formatting :refer [pluralize]]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [type-path]]))

(defn- render-availability [field]
  (if (:always-available? field) [:span.check " ✓"]))

(defn- link-to-type
  "Link to the type passed in. The type should be either a keyword, which names
   the type, or it may be a vector. If it is a vector, it should contain a
   single keyword, and denotes a list of the type named by the keyword.

   The types map defines possible types to link to. If the type is a link to a
   type with a description, a link will be generated. If the target type has no
   description, it is considered trivial, and no link is offered, only the type
   name. If the target type is inline, it is assumed rendered on the same
   page, and an anchor link is generated."
  [type types]
  (let [list-type? (vector? type)
        type-id (if list-type? (first type) type)
        type-name (pluralize (name type-id) (if list-type? 2 1))
        typedef (type-id types)
        path (if (:inline? typedef)
               (str "#" type-name)
               (type-path typedef))]
    (list (if list-type? "list of " "")
          (if path
            [:a {:href path} type-name]
            type-name))))

(defn- render-type-field [field types]
  [:tr
   [:th [:h4.nowrap (list (:name field) (render-availability field))]]
   [:td
    [:h5 (link-to-type (:type field) types)]
    [:p.faded (markdown/render-inline (:description field))]]])

(defn- render-typedef [type types]
  (markdown/render (:description type))
  (if (some :always-available? (:fields type))
    [:p "The check mark " [:span.check "✓"] " indicates that the field always contains a valid non-empty value."])
  [:table.sectioned.mbl
   (map #(render-type-field % types) (:fields type))])

(defn render-type-definition
  "Render the type definition specified by the type map. See the validation data
   structures for a description of the contents. local-types is a list of type
   ids for types that are expected to be found inline on the page. Thus, they
   will be linked as anchors instead of to external pages."
  [type types]
  (list
   [:h3 {:id (name (:id type))} (:name type)]
   (render-typedef type types)))

(defn create-page [type types]
  {:body [:div.wrap
          [:h1 (name (:id type))]
          (render-typedef type types)]})

(defn create-pages
  "Takes a map of types (typically those defined in resources/types.edn) and
   returns a map of url => page function."
  [types]
  (->> (vals types)
       (filter #(or (:description %) (:fields %) (:values %)))
       (map (juxt type-path #(partial create-page % types)))
       (into {})))
