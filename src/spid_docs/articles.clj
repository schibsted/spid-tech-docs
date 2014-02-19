(ns spid-docs.articles
  (:require [clojure.string :as str]
            [spid-docs.formatting :refer [to-html]]
            [spid-docs.highlight :refer [highlight-code-blocks]]
            [spid-docs.layout :as layout]
            [stasis.core :as stasis]))

(defn- create-url [[path _]]
  (str/replace path #"\.md$" "/"))

(defn- create-post [[_ markdown] context]
  (->> markdown
       to-html
       highlight-code-blocks
       (layout/page context "")))

(defn create-pages []
  (->> (stasis/slurp-directory "resources/articles" #"\.md$")
       (map (juxt create-url #(partial create-post %)))
       (into {})))
