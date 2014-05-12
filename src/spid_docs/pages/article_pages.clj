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
     [:h2 "Relevant endpoints"]
     [:ul (map render-relevant-endpoint relevant)])))

(defn- render-contribution [filename]
  [:div.grid-bound
   [:h2 "Help us improve"]
   [:p "Did you spot an error? Or maybe you just have a suggestion for how we can improve? "
    [:a {:href "#disqus_thread"} "Leave a comment"]
    ", or better yet, "
    [:a {:href (str "https://github.com/spid-tech-docs/spid-tech-docs/edit/master/resources/articles"
                    filename)} "send us a pull request"]
    " on GitHub to fix it (in-browser editing, only takes a moment)."]])

(defn create-page [[filename {:keys [title body aside relevant-endpoints] :as article}]]
  (let [body (list [:h1 title] (markdown/render body))
        comments [:div.disqus-comments {:id (article-path filename)}]]
    (if (or aside relevant-endpoints)
      (-> article
          (assoc :split-page? true)
          (assoc :body (list
                        [:div.main [:div.wrap body comments]]
                        [:div.aside
                         [:div.wrap
                          (when aside
                            (markdown/render aside))
                          (when relevant-endpoints
                            (render-relevant-endpoints relevant-endpoints))
                          (render-contribution filename)]])))
      (assoc article :body (list [:div.wrap body comments])))))

(defn create-pages
  "Given a map of markdown files (path => content), generate a map of url =>
  page function. When called, the page function will return markdown rendered as
  HTML."
  [articles]
  (->> articles
       (map (juxt #(article-path (first %)) #(partial create-page %)))
       (into {})))
