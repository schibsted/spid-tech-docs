(ns spid-docs.cultivate.new-endpoints
  "Gather all relevant information about endpoints from a few different sources."
  (:require [clojure.string :as str]
            [spid-docs.homeless :refer [with-optional-keys]]))

(defn- to-simple-dashed-word [path]
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

(defn- create-path-parameter [name endpoint]
  (-> {:name name
       :description ((keyword name) (:parameter_descriptions endpoint))
       :type :path
       :required? true}
      (add-alias-to-param endpoint name)))

(defn- create-query-parameter [name required? endpoint]
  (-> {:name name
       :description ((keyword name) (:parameter_descriptions endpoint))
       :type :query
       :required? required?}
      (add-alias-to-param endpoint name)))

(defn create-filter [filter defaults filter-descriptions]
  {:name filter
   :description (filter-descriptions (keyword filter))
   :default? (.contains defaults filter)})

(defn create-response [response]
  (update-in response [:type] keyword))

(defn success? [response]
  (<= 200 (:status response) 299))

(defn- collect-parameters [required optional pathParameters pagination-descriptions endpoint]
  (concat
   (map #(create-path-parameter % endpoint) pathParameters)
   (map #(create-query-parameter % true endpoint) required)
   (->> optional
        (remove #{"filters"})
        (remove (comp pagination-descriptions keyword))
        (map #(create-query-parameter % false endpoint)))))

(defn- collect-pagination-params [optional pagination-descriptions]
  (->> optional
       (filter (comp pagination-descriptions keyword))
       (map #(create-query-parameter % false {:parameter_descriptions pagination-descriptions}))))

(defn- cultivate-endpoint-1 [endpoint [method details] raw-content]
  (let [{:keys [path category pathParameters valid_output_formats default_output_format deprecated]} endpoint
        {:keys [required optional default_filters filters access_token_types responses]} details
        {:keys [pagination-descriptions filter-descriptions endpoint-descriptions]} raw-content
        id (str (to-simple-dashed-word path) "-" (.toLowerCase (name method)))]
    (with-optional-keys
      {:id (keyword id)
       :path (str "/" path)
       :api-path (str "/api/2/" path)
       :method method
       :name (fix-multimethod-name (:name endpoint) method)
       :description (get endpoint-descriptions (str id ".md"))
       :category {:section (first category) :api (second category)}
       :parameters (collect-parameters required optional pathParameters pagination-descriptions endpoint)
       :?pagination (collect-pagination-params optional pagination-descriptions)
       :?filters (map #(create-filter % default_filters filter-descriptions) filters)
       :response-formats (map keyword valid_output_formats)
       :default-response-format (keyword default_output_format)
       :access-token-types (set (map keyword access_token_types))
       :requires-authentication? (not (empty? access_token_types))
       :responses {:success (create-response (first (filter success? responses)))
                   :failure (map create-response (remove success? responses))}
       :?deprecated deprecated})))

(defn cultivate-endpoint [endpoint raw-content]
  (map #(cultivate-endpoint-1 endpoint % raw-content) (:httpMethods endpoint)))
