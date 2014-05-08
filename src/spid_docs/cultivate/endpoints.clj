(ns spid-docs.cultivate.endpoints
  "Gather all relevant information about endpoints from a few different sources."
  (:require [clojure.string :as str]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [with-optional-keys assoc-non-nil]]
            [spid-docs.cultivate.util :refer [parse-relevant-endpoints]]))

(def verbs
  "Verbs to use for different http methods when cobbling together the
   endpoint name."
  {:GET "Get"
   :POST "Create"
   :DELETE "Delete"})

(defn- fix-multimethod-name [name method]
  "Since names are shared between http methods in the source
   endpoints, we detect descriptions that start with a permutation of
   'get, create or delete' - and replace it with the relevant verb for
   this method."
  (str/replace name #"(?i)^(?:(?:get|create|delete),? )+or (?:get|create|delete) " (str (verbs method) " ")))

(defn- add-alias-to-param [param endpoint]
  "Check if the endpoint has an alias for this parameter, and add it
   to the param map if a match is found."
  (if-let [alias ((keyword (:name param)) (:alias endpoint {}))]
    (assoc param :aliases [alias])
    param))

(defn- create-parameter [name type required? endpoint]
  "Create a parameter map with the given values. Carries its weight by
   looking up description and aliases."
  (-> {:name name
       :description ((keyword name) (:parameter_descriptions endpoint))
       :type type
       :required? required?}
      (add-alias-to-param endpoint)))

(defn create-filter [filter defaults filter-descriptions]
  "Create a filter map with the given values. Looks up description for
   the filter from the central filter descriptions."
  {:name filter
   :description (filter-descriptions (keyword filter))
   :default? (.contains defaults filter)})

(defn create-response [response]
  "Differences between old responses and new responses: The type of the response
   is either a keyword, or a vector with a keyword, signifying a list type. The
   original type is a string."
  (update-in response
             [:type]
             #(let [list-type (second (re-find #"^\[(.*)\]$" (or % "")))
                    map-type (rest (re-find #"^\{(.*) (.*)\}$" (or % "")))]
                (cond
                 list-type [(keyword list-type)]
                 (seq map-type) {(keyword (first map-type)) (keyword (second map-type))}
                 :else (and % (keyword %))))))

(defn success? [response]
  "Is the response status between 200 and 299, ie a success?"
  (<= 200 (:status response) 299))

(def pagination-params
  #{"limit" "offset" "since" "until"})

(defn- collect-parameters [required optional pathParameters endpoint]
  "Gather required, optional and path parameters in one list. The
   optional pagination parameters and 'filters' are removed, since
   they're handled specially elsewhere."
  (concat
   (->> pathParameters (map #(create-parameter % :path true endpoint)))
   (->> required (map #(create-parameter % :query true endpoint)))
   (->> optional
        (remove #{"filters"})
        (remove pagination-params)
        (map #(create-parameter % :query false endpoint)))))

(defn- collect-pagination-params [optional]
  "Gather the pagination parameters in their own list. They are
   treated specially, since we don't want to repeat info about them
   everywhere."
  (->> optional
       (filter pagination-params)
       (map #(create-parameter % :query false {}))))

(defn- match-sample [[^String path sample] endpoint-id]
  "Does this path match the given endpoint-id? If it does, keep it and
   use the file ending as a keyword. Otherwise drop it."
  (let [sample-prefix (str endpoint-id ".")]
    (when (.startsWith path sample-prefix)
      [(keyword (subs path (count sample-prefix))) sample])))

(defn- find-endpoint-samples [endpoint-id sample-responses]
  "Find all sample responses for this endpoint."
  (keep #(match-sample % endpoint-id) sample-responses))

(defn- add-samples [response endpoint-id sample-responses]
  "If there are any sample responses for this endpoint, add them to the response."
  (let [samples (find-endpoint-samples endpoint-id sample-responses)]
    (if (seq samples)
      (assoc response :samples (into {} samples))
      response)))

(def global-failures
  [{:status 401
    :description "You don't have administration rights for this client."
    :type "api-exception"}
   {:status 401
    :description "Your client doesn't have administration rights for this client."
    :type "api-exception"}
   {:status 403
    :description "Client is not authorized to access this API endpoint. Contact SPiD to request access."
    :type "api-exception"}
   {:status 403
    :description "Requesting IP is not whitelisted"
    :type "api-exception"}
   {:status 404
    :description "Unknown client ID"
    :type "api-exception"}
   {:status 404
    :description "Client ID mismatch. The client making the request is no the owner of this resource, and does not have administrative privileges for it."
    :type "api-exception"}
   {:status 420
    :description "Request Ratelimit exceeded"
    :type "api-exception"}])

(defn- get-failure-responses
  "Extracts all failure responses, and adds global ones, such as 403 for any
   endpoint that requires authentication."
  [http-method]
  (->> (if (seq (:access_token_types http-method))
         (conj (:responses http-method) {:status 403
                                         :description "Access token rejected"
                                         :type "api-exception"})
         (:responses http-method))
       (concat global-failures)
       (sort-by :status)
       (remove success?)
       (map create-response)))

(defn- parse-example-params [s]
  (when s
    (->> s
         (str/split-lines)
         (map #(str/split % #":\s*"))
         (into {}))))

(defn- cultivate-endpoint-1 [endpoint [method details] raw-content]
  "Gather a bunch of information from all over to create a map that
   includes everything you could ever want to know about an endpoint."
  (let [{:keys [path category pathParameters valid_output_formats default_output_format deprecated]} endpoint
        {:keys [required optional default_filters filters access_token_types responses]} details
        {:keys [filter-descriptions endpoint-descriptions sample-responses]} raw-content
        endpoint-id (str (to-id-str path) "-" (.toLowerCase (name method)))
        {:keys [introduction success-description relevant-endpoints relevant-types example-params]} (get endpoint-descriptions (str "/" endpoint-id ".md") {})]
    (with-optional-keys
      {:id (keyword endpoint-id)
       :path (str "/" path)
       :api-path (str "/api/2/" path)
       :method method
       :name (fix-multimethod-name (:name endpoint) method)
       :description introduction
       :category {:section (first category) :api (second category)}
       :parameters (collect-parameters required optional pathParameters endpoint)
       :?pagination (collect-pagination-params optional)
       :?filters (map #(create-filter % default_filters filter-descriptions) filters)
       :response-formats (map keyword valid_output_formats)
       :default-response-format (keyword default_output_format)
       :access-token-types (set (map keyword access_token_types))
       :requires-authentication? (not (empty? access_token_types))
       :?relevant-endpoints (parse-relevant-endpoints relevant-endpoints)
       :?relevant-types (when relevant-types (str/split relevant-types #" "))
       :example-params (merge (:example-params raw-content) (parse-example-params example-params))
       :responses {:success (-> success? (filter responses) first create-response
                                (add-samples (str "/" endpoint-id) sample-responses)
                                (assoc-non-nil :description success-description))
                   :failures (get-failure-responses details)}
       :?deprecated deprecated})))

(defn cultivate-endpoint [endpoint raw-content]
  "We define an endpoint as path + http method. The source list of
   endpoints does not. So this function not only cultivates an endpoint,
   it splits it into several endpoints based on the given set of http
   methods."
  (->> (:httpMethods endpoint)
       (remove #(contains? (:endpoint-blacklist raw-content) [(first %) (:path endpoint)]))
       (map #(cultivate-endpoint-1 endpoint % raw-content))))
