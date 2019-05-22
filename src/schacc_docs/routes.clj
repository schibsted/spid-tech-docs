(ns spid-docs.routes
  "A library of path generating functions. Knows how to generate paths to every
   kind of page in the application. These functions where extracted from their
   respective page namespaces as this caused cyclic dependencies (e.g. endpoint
   pages links to API pages, so it requires api-pages, which links to enpoint
   pages, so it requires endpoints ..."
  (:require [clojure.string :as str]
            [spid-docs.formatting :refer [to-id-str]]))

(def prime-categories
  #{"Identity Management"
    "Payment Services"})

(defn api-path
  "Takes a 'service', i.e., the top-level API categorization unit (Identity
   management, payment, etc) and returns its URL."
  [service]
  (if (prime-categories service)
    (str "/#" (to-id-str service))
    "/#other"))

(defn article-path
  "Articles live at the root level, and their path is the same as the
  corresponding file, sans the .md suffix"
  [path]
  (str/replace path #"\.md$" "/"))

(defn endpoint-path
  "Given an endpoint, return the path to the page in the documentation app."
  [endpoint]
  (str "/endpoints/" (name (:method endpoint)) (:path endpoint) "/"))

(defn type-path
  "Given a type (typically one entry from the vector in resources/types.end),
   return its path."
  [type]
  (if (or (:description type) (:fields type) (:values type))
    (str "/types/" (name (:id type)) "/")))
