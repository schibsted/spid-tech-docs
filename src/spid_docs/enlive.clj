(ns spid-docs.enlive
  (:require [hiccup.core :as hiccup]
            [net.cgrand.enlive-html :as enlive])
  (:import java.io.StringReader))

(defn parse [data]
  (if (string? data)
    (enlive/html-snippet data)
    (enlive/html-snippet (hiccup/html data))))

(defn transform
  "TODO: Make this a macro that accepts any number of forms.
   Must be a macro, because pretty much all of enlive is a macro,
   thus cannot be apply-ed"
  [tree selector fn]
  (enlive/sniptest* tree (enlive/transformation selector fn)))

(defn select [selector tree]
  (enlive/select tree selector))
