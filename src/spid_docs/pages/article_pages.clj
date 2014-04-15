(ns spid-docs.pages.article-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [article-path]]))

(defn create-page [[_ article]]
  (let [body (list
              [:h1 (:title article)]
              (markdown/render (:body article)))
        aside (:aside article)]
    (if aside
      (-> article
          (assoc :split-page? true)
          (assoc :body (list
                        [:div.main
                         [:div.wrap
                          body]]
                        [:div.aside
                         [:div.wrap
                          (markdown/render aside)]])))
      (assoc article :body
             [:div.wrap body]))))

(defn create-pages
  "Given a map of markdown files (path => content), generate a map of url =>
  page function. When called, the page function will return markdown rendered as
  HTML."
  [articles]
  (->> articles
       (map (juxt #(article-path (first %)) #(partial create-page %)))
       (into {})))
