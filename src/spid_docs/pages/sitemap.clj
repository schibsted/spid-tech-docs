(ns spid-docs.pages.sitemap
    (:require [clojure.data.xml :as xml]
              [spid-docs.config :refer [get-config]]))

(defn render-url [url]
  [:url
    [:loc {} (str (:tech-docs-base-url (get-config)) url)]])

(defn create-sitemap-page [urls]
    (xml/emit-str
        (xml/sexp-as-element
            [:urlset {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"}
                    (map render-url urls)])))