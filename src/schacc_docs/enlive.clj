(ns spid-docs.enlive
  "Enlive is a templating toolkit that offer very cool things like document
   transformations based on selections. Unfortunately, it's API is notoriously
   hard to work with, so this little wrapper presents the things we use in a
   more digestable way."
  (:require [hiccup.core :as hiccup]
            [net.cgrand.enlive-html :as enlive])
  (:import java.io.StringReader))

(defn parse
  "Parse a string of HTML into a data structure enlive can work with"
  [data]
  (if (string? data)
    (enlive/html-snippet data)
    (enlive/html-snippet (hiccup/html data))))

(defmacro transform
  "Accepts an enlive data structure or a string of HTML followed by pairs of
   selectors and transformations. Returns the transformed markup as a string."
  [tree & forms]
  `(enlive/sniptest* (if (string? ~tree) (parse ~tree) ~tree) (enlive/transformation ~@forms)))

(defn select
  "Given an enlive data structure and an enlive selector, return the matching
  nodes"
  [selector tree]
  (enlive/select tree selector))
