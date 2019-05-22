(ns spid-docs.cultivate.articles
  (:require [spid-docs.cultivate.util :refer [parse-relevant-endpoints]]
            [spid-docs.homeless :refer [assoc-non-nil update-vals with-optional-keys]]))

(defn- vectorify-articles [articles]
  (update-vals articles #(if (map? %) [%] (vec %))))

(defn- cultivate-section [{:keys [body heading title aside relevant-endpoints]}]
  (with-optional-keys
    {:body body
     :?heading (or heading title)
     :?aside aside
     :?relevant-endpoints (parse-relevant-endpoints relevant-endpoints)}))

(defn cultivate-article [article-sections]
  {:title (-> article-sections first :title)
   :frontpage (not (nil? (some :frontpage article-sections)))
   :category (keyword (some :category article-sections))
   :sections (map cultivate-section article-sections)})

(defn cultivate-articles [articles]
  (update-vals (vectorify-articles articles) cultivate-article))
