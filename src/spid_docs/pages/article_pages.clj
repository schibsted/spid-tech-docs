(ns spid-docs.pages.article-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [article-path]]))

(defn create-page [[_ markdown]]
  (let [body (-> markdown markdown/render)]
    {:title (->> body (enlive/parse) (enlive/select [:h1]) first :content (apply str))
     :body [:div.wrap body]}))

(defn create-pages
  "Given a map of markdown files (path => content), generate a map of url =>
  page function. When called, the page function will return markdown rendered as
  HTML."
  [articles]
  (->> articles
       (map (juxt #(article-path (first %)) #(partial create-page %)))
       (into {})))
