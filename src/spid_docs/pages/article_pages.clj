(ns spid-docs.pages.article-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.examples :refer [read-example]]
            [spid-docs.formatting :refer [to-rich-html]]))


(defn article-url [path]
  (str/replace path #"\.md$" "/"))

(defn- insert-examples [markdown]
  (str/replace markdown #"(?m)^:example-code ([^ ]+) ([^ ]+) \"([^\"]+)\""
               (fn [[_ lang path title]]
                 (str "```" lang "\n"
                      (read-example (keyword lang) title path)
                      "\n```\n"))))

(defn create-page [[_ markdown]]
  (let [body (-> markdown insert-examples to-rich-html)]
    {:title (->> body (enlive/parse) (enlive/select [:h1]) first :content (apply str))
     :body body}))

(defn create-pages
  "Given a map of markdown files (path => content), generate a map of url =>
  page function. When called, the page function will return markdown rendered as
  HTML with interpolated examples."
  [articles]
  (->> articles
       (map (juxt #(article-url (first %)) #(partial create-page %)))
       (into {})))
