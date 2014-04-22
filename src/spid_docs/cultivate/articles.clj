(ns spid-docs.cultivate.articles
  (:require [spid-docs.cultivate.util :refer [parse-relevant-endpoints]]
            [spid-docs.homeless :refer [assoc-non-nil update-vals]]))

(defn cultivate-article [article]
  (assoc-non-nil article :relevant-endpoints
                 (parse-relevant-endpoints (:relevant-endpoints article))))

(defn cultivate-articles [articles]
  (update-vals articles cultivate-article))
