(ns spid-docs.pages.type-pages
  (:require [inflections.core :refer [plural]]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [type-path]]))

(defn- render-availability [field]
  (if (:always-available? field) [:span.check " ✓"]))

(defn- get-type-name [type types]
  (or (:name (type types)) (name type)))

(defn- link-to-typedef [type-name typedef]
  (if-let [path (if (:inline? typedef)
               (str "#" type-name)
               (type-path typedef))]
    [:a {:href path} type-name]
    type-name))

(defn- link-to-map-type [type types & [options]]
  (let [[key-property value-type] (first (into [] type))
        type-link (link-to-typedef (plural (get-type-name value-type types))
                                   (value-type types))]
    (list "collection of "
          type-link
          ", as an object with "
          [:code (name key-property)]
          " for property names, and "
          type-link
          " for values")))

(defn- link-to-list-type [type types]
  (list "list of "
        (link-to-typedef (plural (get-type-name (first type) types))
                         ((first type) types))))

(defn link-to-type
  "Link to the type passed in. The type should be either a keyword, which names
   the type, or it may be a vector. If it is a vector, it should contain a
   single keyword, and denotes a list of the type named by the keyword.

   The types map defines possible types to link to. If the type is a link to a
   type with a description, a link will be generated. If the target type has no
   description, it is considered trivial, and no link is offered, only the type
   name. If the target type is inline, it is assumed rendered on the same
   page, and an anchor link is generated."
  [type types]
  (cond
   (vector? type) (link-to-list-type type types)
   (map? type) (link-to-map-type type types)
   :else (link-to-typedef (get-type-name type types) (type types))))

(defn- render-type-field [field types]
  [:tr
   [:th [:h4.nowrap (list (:name field) (render-availability field))]]
   [:td
    [:h5 (link-to-type (:type field) types)]
    [:p.faded (markdown/render-inline (:description field))]]])

(defn- render-type-value [val]
  [:tr
   [:th [:h4.nowrap
         [:code
          (if (string? (:value val)) "\"")
          (:value val)
          (if (string? (:value val)) "\"")]]]
   [:td
    [:p.faded (markdown/render-inline (:description val))]]])

(defn- render-typedef [type types]
  (list
   (markdown/render (:description type))
   (if (some :always-available? (:fields type))
     [:p "The check mark " [:span.check "✓"] " indicates that the field always contains a valid non-empty value."])
   (if (= :enum (:rendering type))
     [:p "An enum, with the following possible values:"])
   [:table.sectioned.mbl
    ;; Render "both" fields and values - only either one will be present in any
    ;; given type.
    (map #(render-type-field % types) (:fields type))
    (map render-type-value (:values type))]))

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
