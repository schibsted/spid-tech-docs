(ns spid-docs.enlive
  (:require [hiccup.core :as hiccup]
            [net.cgrand.enlive-html :as enlive])
  (:import java.io.StringReader))

(defn parse [data]
  (cond
   (string? data) (enlive/html-snippet data)
   (or (list? data) (vector? data)) (enlive/html-snippet (hiccup/html data))))

(defn transform [tree selector fn]
  "TODO: Make this a macro that accepts any number of forms.
   Must be a macro, because pretty much all of enlive is a macro,
   thus cannot be apply-ed"
  (enlive/sniptest* tree (enlive/transformation selector fn)))

(defn select [selector tree]
  (enlive/select tree selector))
