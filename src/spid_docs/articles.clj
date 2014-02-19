(ns spid-docs.articles
  (:require [clojure.string :as str]
            [spid-docs.formatting :refer [to-html]]
            [stasis.core :as stasis]))

(defn- create-url [[path _]]
  (str/replace path #"\.md$" "/"))

(defn- create-post [[_ markdown]]
  (to-html markdown))

(defn create-pages []
  (->> (stasis/slurp-directory "resources/articles" #"\.md$")
       (map (juxt create-url create-post))
       (into {})))
