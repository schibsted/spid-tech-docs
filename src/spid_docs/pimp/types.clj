(ns spid-docs.pimp.types
  "Converts <spid-types> element into list with
  client types. Used in endpoint pages to show which client
  type has access to said endpoint. Content between opening
  and closing tags will determine where checkboxes are shown."
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]))

(def client-types 
  "Defines the client-types. The key has to be used in the markup.
  The value will be displayed on the page."
  { "W" "Web"
    "M" "Mobile"
    "B" "Backend"
    "A" "Admin" })

(defn substring [sub st]
  (not= (.indexOf st sub) -1))

(defn- generate-types [contents]
  "Generate the HTML markup used to replace the spid-types element
  using hiccup."
  (hiccup/html 
    [:ul {:class "types-list"}
      (for [client-type (reverse client-types)] 
        [:li [:div {:class "type-radio"} [:span {:class "check"} (if (substring (key client-type) contents) " ✓")]] (val client-type)])]))

(defn create-types [html]
  "Find all occurrences of the element and replace with 
  the corresponding HTML markup."
  (str/replace html #"(?sm)<spid-types>(.+?)</spid-types>" (fn [[_ contents]] (-> contents generate-types))))
