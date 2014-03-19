(ns spid-docs.pages.api-pages
  "Functions to work with API categorization levels."
  (:require [spid-docs.pages.endpoint-pages :as ep]
            [spid-docs.routes :refer [api-path endpoint-path]]))

(defn- render-api [api]
  (list [:h2 (:title api)]
        [:p (:description api)]
        [:ul (map #(vector :li
                           [:a {:href (endpoint-path %)}
                            (str "/" (:path %))]) (:endpoints api))]))

(defn create-page [service]
  {:title (:title service)
   :body [:div.wrap
          [:h1 (:title service)]
          (map render-api (:apis service))]})

(defn create-pages
  "Given a list of services (typically those found in resources/apis.edn),
  generate a map of url => page function."
  [services]
  (->> services
       (map (juxt api-path #(partial create-page %)))
       (into {})))
