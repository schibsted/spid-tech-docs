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

(def filter-descriptions {"merchant" "Show results for entire merchant, not just current client."})

(defn create-filter [default filter]
  {:name filter
   :description (filter-descriptions filter)
   :default? (.contains default filter)})

(defn- cultivate-endpoint-1 [endpoint [method details]]
  (let [{:keys [path name category pathParameters valid_output_formats default_output_format]} endpoint
        {:keys [required optional default_filters filters access_token_types]} details]
    (with-optional-keys
      {:id (-> path generate-id)
       :path (str "/" path)
       :api-path (str "/api/2/" path)
       :method method
       :name (fix-multimethod-name name method)
       :category {:section (first category)
                  :api (second category)}
       :parameters (concat
                    (map (partial create-path-parameter endpoint) pathParameters)
                    (map (partial create-query-parameter endpoint true) required)
                    (->> optional
                         (remove #{"filters"})
                         (remove (comp pagination-params keyword))
                         (map (partial create-query-parameter endpoint false))))
       :?pagination (->> optional
                         (filter (comp pagination-params keyword))
                         (map (partial create-query-parameter {:parameter_descriptions pagination-params} false)))
       :?filters (map (partial create-filter default_filters) filters)
       :response-formats (map keyword valid_output_formats)
       :default-response-format (keyword default_output_format)
       :access-token-types (set (map keyword access_token_types))
       :requires-authentication? (not (empty? access_token_types))})))

(defn cultivate-endpoint [endpoint]
  (map (partial cultivate-endpoint-1 endpoint) (:httpMethods endpoint)))
