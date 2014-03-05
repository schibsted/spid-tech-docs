(ns spid-docs.confluence
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [net.cgrand.enlive-html :refer [sniptest html-resource select has] :as enlive]
            [spid-docs.enlive :refer [parse]]
            [ring.util.codec :refer [url-decode]])
  (:import [org.apache.commons.lang StringEscapeUtils]))

(defn- replace-local-anchors [node]
  (if (-> node :attrs :href (.startsWith "#"))
    {:tag :ac:link
     :attrs {:ac:anchor (-> node :attrs :href (subs 1) url-decode)}
     :content [{:tag :ac:plain-text-link-body
                :content [{:tag :CDATA
                           :content (:content node)}]}]}
    node))

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

(defn- fix-cdata-escapings
  "Enlive doesn't support emitting proper CDATA.
   Fixing escaping with regexen like a pro. Like a pro!"
  [html]
  (str/replace html #"(?s)<CDATA>(.*?)</CDATA>"
               (fn [[_ contents]]
                 (str "<![CDATA["
                      (StringEscapeUtils/unescapeHtml contents)
                      "]]>"))))

(defn to-storage-format [s]
  (-> (sniptest s
                [:a] replace-local-anchors
                [:pre] replace-code-snippets
                [:div.tabs] replace-tabs
                [:div.line] replace-lines
                [:div] enlive/unwrap)
      (fix-cdata-escapings)))

(defn- only [xs]
  (if (next xs)
    (throw (Exception. "Expected only one h1 in document."))
    (first xs)))

(defn- extract-title [html]
  (->> (select (parse html) [:h1]) only :content (apply str)))

(defn- drop-node [node])

(defn- change-tag [type node]
  (assoc node :tag type))

(defn- upgrade-headers-skip-title [html]
  (-> (sniptest html
                [:h1] drop-node
                [:h2] (partial change-tag :h1)
                [:h3] (partial change-tag :h2)
                [:h4] (partial change-tag :h3)
                [:h5] (partial change-tag :h4))))

(defn upgrade-headers [html]
  {:title (extract-title html)
   :body (upgrade-headers-skip-title html)})
