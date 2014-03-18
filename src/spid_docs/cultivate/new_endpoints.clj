(ns spid-docs.cultivate.new-endpoints
  (:require [clojure.string :as str]
            [spid-docs.homeless :refer [with-optional-keys]]))

(defn- generate-id [path]
  (-> path
      (str/replace #"[^a-zA-Z]+" "-")
      (str/replace #"-$" "")))

(def verbs {:GET "Get"
            :POST "Create"
            :DELETE "Delete"})

(defn- fix-multimethod-name [name method]
  (str/replace name #"(?i)^(?:(?:get|create|delete),? )+or (?:get|create|delete) " (str (verbs method) " ")))

(defn- add-alias-to-param [param endpoint name]
  (if-let [alias ((keyword name) (:alias endpoint {}))]
    (assoc param :aliases [alias])
    param))

(defn- create-path-parameter [endpoint name]
  (-> {:name name
       :description ((keyword name) (:parameter_descriptions endpoint))
       :type :path
       :required? true}
      (add-alias-to-param endpoint name)))

(defn- create-query-parameter [endpoint required? name]
  (-> {:name name
       :description ((keyword name) (:parameter_descriptions endpoint))
       :type :query
       :required? required?}
      (add-alias-to-param endpoint name)))

(def pagination-params {:limit  "Return no more than this number of results."
                        :offset "Skip this many results."
                        :since  "Return only results on or after this date."
                        :until  "Return only results up to and including this date."})

(defn- cultivate-endpoint-1 [endpoint [method details]]
  (with-optional-keys
    {:id (-> (:path endpoint) generate-id)
     :path (str "/" (:path endpoint))
     :api-path (str "/api/2/" (:path endpoint))
     :method method
     :name (fix-multimethod-name (:name endpoint) method)
     :category {:section (first (:category endpoint))
                :api (second (:category endpoint))}
     :parameters (concat
                  (map (partial create-path-parameter endpoint) (:pathParameters endpoint))
                  (map (partial create-query-parameter endpoint true) (:required details))
                  (->> details :optional
                       (remove #{"filters"})
                       (remove (comp pagination-params keyword))
                       (map (partial create-query-parameter endpoint false))))
     :?pagination (->> details :optional
                       (filter (comp pagination-params keyword))
                       (map (partial create-query-parameter {:parameter_descriptions pagination-params} false)))
     :response-formats (map keyword (:valid_output_formats endpoint))
     :default-response-format (keyword (:default_output_format endpoint))}))

(defn cultivate-endpoint [endpoint]
  (map (partial cultivate-endpoint-1 endpoint) (:httpMethods endpoint)))
