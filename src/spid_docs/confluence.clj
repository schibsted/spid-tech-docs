(ns spid-docs.confluence
  (:require [clojure.string :as str]
            [hiccup.core :as hiccup]
            [net.cgrand.enlive-html :as enlive :refer [sniptest select]]
            [ring.util.codec :refer [url-decode]]
            [spid-docs.enlive :refer [parse]])
  (:import [org.apache.commons.lang StringEscapeUtils]))

(defn- load-page [href pages]
  (if-let [get-page (pages href)]
    (get-page)
    (throw (Exception. (str "Can't link to unknown page " href)))))

(defn- replace-local-anchors [pages node]
  (let [href (-> node :attrs :href)]
    (cond
     (.startsWith href "#") {:tag :ac:link
                             :attrs {:ac:anchor (-> node :attrs :href (subs 1) url-decode)}
                             :content [{:tag :ac:plain-text-link-body
                                        :content [{:tag :CDATA
                                                   :content (:content node)}]}]}

     (.startsWith href "/") {:tag :ac:link
                             :content [{:tag :ri:page
                                        :attrs {:ri:content-title (:title (load-page href pages))}}
                                       {:tag :ac:plain-text-link-body
                                        :content [{:tag :CDATA
                                                   :content (:content node)}]}]}
     :else node)))

(defn- code-snippet? [node]
  (and (= (-> node :tag) :pre)
       (= (-> node :content count) 1)
       (= (-> node :content first :tag) :code)))

(def supported-languages
  #{"actionscript3" "applescript" "bash" "c#" "cpp" "css" "coldfusion" "delphi"
    "diff" "erl" "groovy" "xml" "java" "jfx" "js" "php" "perl" "text"
    "powershell" "py" "ruby" "sql" "sass" "scala" "vb"})

(defn- replace-code-snippets [node]
  (if (code-snippet? node)
    (let [language (-> node :content first :attrs :class)
          content (-> node :content first :content)]
      {:tag :ac:structured-macro
       :attrs {:ac:name "code"}
       :content [(when (supported-languages language)
                   {:tag :ac:parameter
                    :attrs {:ac:name "language"}
                    :content [language]})
                 {:tag :ac:plain-text-body
                  :content [{:tag :CDATA
                             :content content}]}]})
    node))

(defn- create-tab [[header div]]
  {:tag :ac:structured-macro
   :attrs {:ac:name "auitabspage"}
   :content [{:tag :ac:parameter
              :attrs {:ac:name "title"}
              :content [(->> header :content (apply str))]}
             {:tag :ac:rich-text-body
              :content (:content div)}]})

(defn- replace-tabs [node]
  {:tag :ac:structured-macro
   :attrs {:ac:name "auitabs"}
   :content [{:tag :ac:parameter
              :attrs {:ac:name "direction"}
              :content ["horizontal"]}
             {:tag :ac:rich-text-body
              :content (map create-tab (->> node :content (partition 2)))}]})

(defn- create-section [{:keys [content]}]
  (when content
    {:tag :ac:layout-cell
     :content content}))

(defn- replace-lines [node]
  (let [sections (keep create-section (:content node))]
    {:tag :ac:layout
     :content [ {:tag :ac:layout-section
                 :attrs {:ac:type (case (count sections)
                                    1 "single"
                                    2 "two_equal"
                                    3 "three_equal"
                                    (throw (Exception. (str "Unsupported number of sections: " (count sections)))))}
                 :content sections}]}))

(defn- fix-cdata-escapings [html]
  "Enlive doesn't support emitting proper CDATA.
   Fixing escaping with regexen like a pro. Like a pro!"
  (str/replace html #"(?s)<CDATA>(.*?)</CDATA>"
               (fn [[_ contents]]
                 (str "<![CDATA["
                      (StringEscapeUtils/unescapeHtml contents)
                      "]]>"))))

(defn- replace-checkmarks [html]
  (str/replace html #"✓" "Yes"))

(defn- only [xs]
  (if (next xs)
    (throw (Exception. "Expected only one h1 in document."))
    (first xs)))

(defn- extract-title [html]
  (->> (select (parse html) [:h1]) only :content (apply str)))

(defn- drop-node [node])

(defn- change-tag [type node]
  (assoc node :tag type))

(defn- replace-with-h1 [node]
  [{:tag :br}
   (change-tag :h1 node)])

(defn- wrap-in-layout [s]
  (if (.contains s "<ac:layout>")
    (str "<ac:layout><ac:layout-section ac:type=\"single\"><ac:layout-cell>"
         (-> s
             (str/replace #"<ac:layout>" "</ac:layout-cell></ac:layout-section>")
             (str/replace #"</ac:layout>" "<ac:layout-section ac:type=\"single\"><ac:layout-cell>"))
         "</ac:layout-cell></ac:layout-section></ac:layout>")
    s))

(defn to-confluence-url [type [url _]]
  (if (= url "/")
    (str "/index.csf." type)
    (-> url
        (str/replace #"/$" "")
        (str ".csf." type))))

(defn to-storage-format
  ([s] (to-storage-format s {}))
  ([s pages]
     {:title (extract-title s)
      :body (-> (sniptest s
                          [:a] #(replace-local-anchors pages %)
                          [:pre] replace-code-snippets
                          [:div.tabs] replace-tabs
                          [:div.line] replace-lines
                          [:div] enlive/unwrap
                          [:h1] drop-node
                          [:h2] replace-with-h1
                          [:h3] (partial change-tag :h2)
                          [:h4] (partial change-tag :h3)
                          [:h5] (partial change-tag :h4))
                (fix-cdata-escapings)
                (wrap-in-layout)
                (replace-checkmarks))}))

(defn create-confluence-export [pages [_ get-page] _]
  (-> (get-page) :body hiccup/html (to-storage-format pages) :body))

(defn create-confluence-export-html [pages [_ get-page] _]
  (let [page (-> (get-page) :body hiccup/html (to-storage-format pages))]
    (hiccup/html
     [:link {:rel "stylesheet" :type "text/css" :href "/styles/export.css"}]
     [:input {:type "text" :value (:title page)}]
     [:textarea (:body page)]
     [:script {:src "/scripts/export.js"}])))
