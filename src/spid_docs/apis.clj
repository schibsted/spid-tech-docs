(ns spid-docs.apis
  (:require [clojure.string :as str]
            [spid-docs.endpoints :as ep]))

(defn get-name [kw]
  (->> (-> (str kw)
           (subs 1)
           (str/split #"-"))
       (map str/capitalize)
       (str/join " ")))

(defn api-index-url [id]
  (str "/apis/" (subs (str id) 1)))

(defn- to-page-url [[path _]]
  (api-index-url path))

(defn belongs-to [service-id api-id]
  (fn [endpoint]
    (->> endpoint
         :categorization
         (some #(= [service-id api-id] %)))))

(defn- render-api [api-id description endpoints]
  (list [:h2 (get-name api-id)]
        [:p description]
        [:ul (map #(vector :li
                           [:a {:href (ep/endpoint-path %)}
                            (str "/" (:path %))]) endpoints)]))

(defn- get-endpoints [endpoints service-id api-id]
  (filter (belongs-to service-id api-id) endpoints))

(defn- create-index [[path apis] endpoints]
  {:title (get-name path)
   :body (list [:h1 (get-name path)]
               (map #(render-api (first %) (second %) (get-endpoints endpoints path (first %))) apis))})

(defn create-pages [services endpoints]
  (->> services
       (map (juxt to-page-url #(partial create-index % endpoints)))
       (into {})))
