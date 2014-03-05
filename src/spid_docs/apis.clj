(ns spid-docs.apis
  (:require [clojure.string :as str]
            [spid-docs.endpoints :as ep]))

(defn get-name [kw]
  (->> (-> (str kw)
           (subs 1)
           (str/split #"-"))
       (map str/capitalize)
       (str/join " ")))

(defn api-index-url [service]
  (str "/apis/" (subs (str (:id service)) 1)))

(defn- render-api [api]
  (list [:h2 (get-name (:id api))]
        [:p (:description api)]
        [:ul (map #(vector :li
                           [:a {:href (ep/endpoint-path %)}
                            (str "/" (:path %))]) (:endpoints api))]))

(defn- create-index [service]
  {:title (get-name (:id service))
   :body (list [:h1 (:title service)]
               (map render-api (:apis service)))})

(defn create-pages [services]
  (->> services
       (map (juxt api-index-url #(partial create-index %)))
       (into {})))
