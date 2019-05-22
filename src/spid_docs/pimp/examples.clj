(ns spid-docs.pimp.examples
  "Functions to facilitate extracting code examples from the example repo."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [spid-docs.enlive :as enlive]
            [spid-docs.homeless :refer [unindent]]))

(def examples-dir
  "Examples directory, relative to resources/"
  "example-repos/")

(defn- find-example
  "Finds a code example in the provided code with the given delimiters."
  [start-delim end-delim code]
  (->> (str/split code #"\n")
       (drop-while #(not= start-delim (str/trim %)))
       (drop 1)
       (take-while #(not= end-delim (str/trim %)))
       (unindent)
       (str/join "\n")))

(defn- strip-example-dir
  "Returns the relative path to a file inside the example directory"
  [path]
  (->> (str/split path #"/")
       (drop 2)
       (str/join "/")))

(defn- with-missing-example-warning
  "Makes sure a warning is thrown when trying to refer a missing code example."
  [path title example]
  (if (seq example)
    example
    (throw (Exception. (format "No example '%s' found in %s" title path)))))

(defmulti create-example
  "Render code example. Will throw an exception if trying to render a code
   example that does not exist"
  (fn [lang path title code] lang))

(defn- find-html-example [title code]
  (find-example (format "<!-- %s -->" title) "<!---->" code))

(defmethod create-example :php [_ path title code]
  (let [php-example (find-example (format "/** %s */" title) "/**/" code)
        html-example (find-html-example title code)]
    (with-missing-example-warning path title
      (cond
       (seq php-example) (str "<?php // " (strip-example-dir path) "\n" php-example)
       (seq html-example) (str "<!-- " (strip-example-dir path) " -->\n" html-example)
       :else nil))))

(defmethod create-example :java [_ path title code]
  (with-missing-example-warning path title
    (find-example (format "/** %s */" title) "/**/" code)))

(defmethod create-example :clj [_ path title code]
  (with-missing-example-warning path title
    (find-example (format ";;; %s" title) ";;;" code)))

(defmethod create-example :html [_ path title code]
  (with-missing-example-warning path title
    (find-html-example title code)))

(defmethod create-example :sh [_ path title code]
  (-> (with-missing-example-warning path title
        (find-example (format "### %s" title) "###" code))
      (str/replace #" --silent\b" "")
      (str/replace #"\$server\b" "https://identity-pre.schibsted.com")))

(defn- read-example-file [lang repo path]
  (let [full-path (str examples-dir repo path)
        resource (io/resource full-path)]
    (if resource
      (slurp resource)
      (throw (Exception. (format "No example file %s found." full-path))))))

(defn read-example [lang repo title path]
  (create-example lang path title
                  (read-example-file lang repo path)))

(def example-dir-folders
  {:java "java"
   :php "php"
   :clj "clj"
   :sh "curl"})

(defn- inline-example [node]
  (let [attrs (:attrs node)
        lang (:lang attrs)]
    {:tag :pre
     :attrs {}
     :content [{:tag :code
                :attrs {:class lang}
                :content (read-example (keyword lang)
                                       (or (:repo attrs)
                                           (example-dir-folders (keyword lang))) ;; if no repo is given, use lang to look it up
                                       (:title attrs)
                                       (:src attrs))}]}))

(defn inline-examples
  "Finds all <spid-example> tags, finds the referred examples and returns the text
   with the example tags replaced with the actual examples."
  [html]
  (enlive/transform (enlive/parse html) [:spid-example] inline-example))
