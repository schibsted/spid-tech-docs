(ns spid-docs.pages.frontpage
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [spid-docs.formatting :refer [columnize to-id-str]]
            [spid-docs.routes :refer [endpoint-path prime-categories article-path]]))

(def frontpage-columns 3)

(def category-render-order ["Identity Management"
                            "Payment Services"
                            "Other"])

(defn- endpoint-count
  "The number of endpoints in an API (e.g. 'Login API') is used to evenly
   distribute APIs across columns."
  [api]
  (-> api :endpoints count))

(defn- columnize-apis
  "Returns a list of lists of apis. Each list represents a column and their
   content is a list of apis to render in the column."
  [apis]
  (columnize apis frontpage-columns endpoint-count))

(defn- render-api
  "Renders a single API: the name and a linked list of endpoints"
  [api]
  (list
   [:h4 {:id (to-id-str (:api api))}
    (if (:url api)
      [:a {:href (:url api)} (:api api)]
      (:api api))]
   [:ul (->> (:endpoints api)
             (map #(vector :li [:a {:href (endpoint-path %)}
                                [:code (name (:method %))] " " (:path %)])))]))

(defn- render-api-column [num apis]
  [:div {:class (if (= (inc num) frontpage-columns) "lastUnit" "unit s1of3")}
   (map render-api apis)])

(defn render-service-apis
  "Render the services (e.g. 'Identity management') with all APIs distributed
   across a number of columns (see frontpage-columns towards the top)."
  [category service-apis]
  [:div.line [:h2 category]
   (map-indexed render-api-column (columnize-apis service-apis))])

(defn- remove-deprecated-endpoints [api]
  (let [endpoints (remove :deprecated (:endpoints api))]
    (when (seq endpoints)
      (assoc api :endpoints endpoints))))

(defn- collapse-other-categories [api]
  (if (prime-categories (:category api))
    api
    (assoc api :category "Other")))

(defn render-article-link [[path article]]
  [:li [:a {:href (article-path path)} (:title article)]])

(defn render-article-list [num articles]
  [:div {:class (if (= (inc num) frontpage-columns) "lastUnit" "unit s1of4")}
   [:ul
    (map render-article-link articles)]])

(defn create-article-hrefs [articles]
  (map-indexed render-article-list (columnize articles frontpage-columns)))

(def categories
     {:payment "Payment"
      :analytics "Analytics and Insight"
      :api-integration "API and Integration"
      :self-service "Self service and support"
      :how-tos "FAQs and HOWTOs"
      :flows "Navigation flows"
      })

(defn- frontpage-articles [articles]
  (into {} (filter #(:frontpage (second %)) articles)))

(defn- render-category-with-articles [[category articles]]
  (if (categories category)
    [:div {:class (if (= (last categories) category) "lastUnit" "unit s1of3")}
     [:h3 (categories category)]
     [:ul
      (map render-article-link articles)]]))

(defn- create-articles-in-categories [articles]
  (map render-category-with-articles (group-by (fn [[path article]] (:category article)) articles)))

(defn create-page [apis articles]
  {:title "Schibsted account API Documentation"
   :body (let [apis-by-category (->> apis vals
                                     (keep remove-deprecated-endpoints)
                                     (map collapse-other-categories)
                                     (group-by :category))]
           (-> (slurp (io/resource "frontpage.html"))
               (str/replace "<apis-by-category/>"
                            (hiccup/html (map #(render-service-apis % (apis-by-category %)) category-render-order)))
               (str/replace "<list-of-articles-in-categories/>" (hiccup/html (create-articles-in-categories (frontpage-articles articles))))
               (str/replace "<list-of-articles/>"
                            (hiccup/html (create-article-hrefs (frontpage-articles articles))))))})

(defn- remove-non-deprecated-endpoints [api]
  (let [endpoints (filter :deprecated (:endpoints api))]
    (when (seq endpoints)
      (assoc api :endpoints endpoints))))

(defn create-deprecated-endpoints-page [apis]
  {:title "Deprecated endpoints"
   :body (let [apis-by-category (->> apis vals
                                     (keep remove-non-deprecated-endpoints)
                                     (map collapse-other-categories)
                                     (group-by :category))]
           [:div.wrap
            [:h1 "Deprecated endpoints"]
            [:p "These endpoints should no longer be used."]
            (map #(render-service-apis % (apis-by-category %)) category-render-order)])})
