(ns spid-docs.confluence
  (:require [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [net.cgrand.enlive-html :refer [sniptest html-resource select has]])
  (:import [org.apache.commons.lang StringEscapeUtils]))

(defn- replace-local-anchors [node]
  (if (-> node :attrs :href (.startsWith "#"))
    {:tag :ac:link
     :attrs {:ac:anchor "Working examples"}
     :content [{:tag :ac:plain-text-link-body
                :content [{:tag :CDATA
                           :content (:content node)}]}]}
    node))

(defn- code-snippet? [node]
  (and (= (-> node :tag) :pre)
       (= (-> node :content count) 1)
       (= (-> node :content first :tag) :code)))

(defn- replace-code-snippets [node]
  (if (code-snippet? node)
    {:tag :ac:structured-macro
     :attrs {:ac:name "code"}
     :content [{:tag :ac:parameter
                :attrs {:ac:name "language"}
                :content [(-> node :content first :attrs :class)]}
               {:tag :ac:plain-text-body
                :content [{:tag :CDATA
                           :content (-> node :content first :content)}]}]}
    node))

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
       [:pre] replace-code-snippets)
      (fix-cdata-escapings)))
