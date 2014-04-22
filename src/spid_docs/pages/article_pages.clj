(ns spid-docs.pages.article-pages
  (:require [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.formatting :refer [pluralize]]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [article-path]]))

(defn- render-relevant-endpoint [{:keys [method path]}]
  [:li [:a {:href (str "/endpoints/" (name method) path)}
        [:code method] " " path]])

(defn- render-relevant-endpoints [relevant]
  (when relevant
    (list
     [:h2 (pluralize "Relevant endpoint" (count relevant))]
     [:ul (map render-relevant-endpoint relevant)])))

(defn create-page [[_ {:keys [title body aside relevant-endpoints] :as article}]]
  (let [body (list [:h1 title] (markdown/render body))]
    (if (or aside relevant-endpoints)
      (-> article
          (assoc :split-page? true)
          (assoc :body (list
                        [:div.main [:div.wrap body]]
                        [:div.aside
                         [:div.wrap
                          (when aside
                            (markdown/render aside))
                          (when relevant-endpoints
                            (render-relevant-endpoints relevant-endpoints))]])))
      (assoc article :body [:div.wrap body]))))

(defn create-pages
  "Given a map of markdown files (path => content), generate a map of url =>
  page function. When called, the page function will return markdown rendered as
  HTML."
  [articles]
  (->> articles
       (map (juxt #(article-path (first %)) #(partial create-page %)))
       (into {})))
