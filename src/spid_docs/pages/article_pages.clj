(ns spid-docs.pages.article-pages
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]
            [spid-docs.enlive :as enlive]
            [spid-docs.formatting :refer [pluralize]]
            [spid-docs.pimp.markdown :as markdown]
            [spid-docs.routes :refer [article-path endpoint-path]]))

(defn- render-relevant-endpoint [{:keys [method path] :as endpoint}]
  [:li [:a {:href (endpoint-path endpoint)}
        [:code method] " " path]])

(defn- render-relevant-endpoints [relevant]
  (when relevant
    (list
     [:h2 "Relevant endpoints"]
     [:ul (map render-relevant-endpoint relevant)])))

(defn- render-contribution [filename]
  (list
   [:h2 "Help us improve"]
   [:p "Did you spot an error? Or maybe you just have a suggestion for how we can improve? "
    [:a {:href "#disqus_thread"} "Leave a comment"]
    ", or better yet, "
    [:a {:href (str "https://github.com/schibsted/spid-tech-docs/edit/master/resources/articles"
                    filename)
         :target "_blank"} "send us a pull request"]
    " on GitHub to fix it (in-browser editing, only takes a moment)."]))

(defn- format-article-section [{:keys [heading aside body relevant-endpoints contributions]}]
  [:div.section
   [:div.main
    [:div.wrap
     (list (when heading [:h1 heading])
           (markdown/render body))]]
   [:div.aside
    [:div.wrap
     (when aside (markdown/render aside))
     (when relevant-endpoints (render-relevant-endpoints relevant-endpoints))
     (when contributions contributions)]]])

(defn- get-comment-section [filename]
  {:body (hiccup/html [:div.disqus-comments {:id (article-path filename)}])})

(defn create-page [[filename {:keys [title sections]}]]
  {:split-page? true
   :title title
   :body (list
          (->> (assoc-in (vec sections) [0 :contributions] (render-contribution filename))
               (map format-article-section)
               (interpose [:div.separator]))
          (format-article-section (get-comment-section filename)))})

(defn create-pages
  "Given a map of mapdown files (path => content), generate a map of url =>
  page function. When called, the page function will return markdown rendered as
  HTML."
  [articles]
  (->> articles
       (map (juxt #(article-path (first %)) #(partial create-page %)))
       (into {})))
