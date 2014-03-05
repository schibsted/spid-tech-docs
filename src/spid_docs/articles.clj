(ns spid-docs.articles
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.examples :refer [read-example]]
            [spid-docs.formatting :refer [to-rich-html]]))

(defn- to-page-url [[path _]]
  (str/replace path #"\.md$" "/"))

(defn- insert-examples [markdown]
  (str/replace markdown #"(?m)^:example-code ([^ ]+) ([^ ]+) \"([^\"]+)\""
               (fn [[_ lang path title]]
                 (str "```" lang "\n"
                      (read-example (keyword lang) title path)
                      "\n```\n"))))

(defn- create-post [[_ markdown]]
  (let [body (-> markdown insert-examples to-rich-html)]
    {:title (->> body (enlive/parse) (enlive/select [:h1]) first :content (apply str))
     :body body}))

(defn create-pages [articles]
  (->> articles
       (map (juxt to-page-url #(partial create-post %)))
       (into {})))
