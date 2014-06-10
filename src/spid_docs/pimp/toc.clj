(ns spid-docs.pimp.toc
  (:require [clojure.string :as str]
            [net.cgrand.enlive-html :refer [sniptest html-resource select]]))

(defn- toc-item [heading]
  (str "<li><a href=\"#"
       (-> heading :attrs :id)
       "\">"
       (-> heading :content first)
       "</a></li>"))

(defn- find-headings-in-main [html]
  (-> html
      java.io.StringReader.
      html-resource
      (select [:.main :h2])))

(defn- generate-toc [html]
  (apply str
         (concat ["<ul>"]
                 (map toc-item (find-headings-in-main html))
                 ["</ul>"])))

(defn create-toc [html]
  (str/replace html #"<spid-toc></spid-toc>" (fn [_] (generate-toc html))))
