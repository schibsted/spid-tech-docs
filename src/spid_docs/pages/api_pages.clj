(ns spid-docs.pages.api-pages
  "Functions to work with API categorization levels."
  (:require [clojure.string :as str]
            [spid-docs.pages.endpoint-pages :as ep]))

(defn api-url
  "Takes a 'service', i.e., the top-level API categorization unit (Identity
   management, payment, etc) and returns its URL."
  [service]
  (str "/apis/" (subs (str (:id service)) 1)))

(defn- render-api [api]
  (list [:h2 (:title api)]
        [:p (:description api)]
        [:ul (map #(vector :li
                           [:a {:href (ep/endpoint-url %)}
                            (str "/" (:path %))]) (:endpoints api))]))

(defn create-page [service]
  {:title (:title service)
   :body (list [:h1 (:title service)]
               (map render-api (:apis service)))})

(defn create-pages
  "Given a list of services (typically those found in resources/apis.edn),
  generate a map of url => page function."
  [services]
  (->> services
       (map (juxt api-url #(partial create-page %)))
       (into {})))
