(ns spid-docs.cultivate.endpoints
  "Gather all relevant information about endpoints from a few different sources."
  (:require [clojure.string :as str]
            [spid-docs.formatting :refer [to-id-str]]
            [spid-docs.homeless :refer [with-optional-keys assoc-non-nil]]))

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

(defn- collect-parameters [required optional pathParameters pagination-descriptions endpoint]
  "Gather required, optional and path parameters in one list. The
   optional pagination parameters and 'filters' are removed, since
   they're handled specially elsewhere."
  (concat
   (->> pathParameters (map #(create-parameter % :path true endpoint)))
   (->> required (map #(create-parameter % :query true endpoint)))
   (->> optional
        (remove #{"filters"})
        (remove (comp pagination-descriptions keyword))
        (map #(create-parameter % :query false endpoint)))))

(defn- collect-pagination-params [optional pagination-descriptions]
  "Gather the pagination parameters in their own list, getting
   descriptions from a central location instead of on the endpoint
   itself. This is to avoid repeating descriptions for pagination
   everywhere."
  (->> optional
       (filter (comp pagination-descriptions keyword))
       (map #(create-parameter % :query false {:parameter_descriptions pagination-descriptions}))))

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
    :description "Not an admin for client. If you are an admin user and are trying to administrate a different client then the one you logged in for and you do not have admin access to it."
    :type "api-exception"}
   {:status 401
    :description "Not an admin client for client. Non admin user trying to administer a different client and the client you logged in with does not have admin rights over this client."
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

(defn- parse-see-also-line [l]
  "Takes a string like: GET /the/path and makes a map out of it."
  (let [[method path] (str/split l #" " 2)]
    {:method (keyword method)
     :path path}))

(defn- parse-see-also [see-also]
  "Takes a list of newline separated endpoint signatures, and turns it into a map."
  (when see-also
   (->> see-also
        str/trim
        str/split-lines
        (map parse-see-also-line))))

(defn- cultivate-endpoint-1 [endpoint [method details] raw-content]
  "Gather a bunch of information from all over to create a map that
   includes everything you could ever want to know about an endpoint."
  (let [{:keys [path category pathParameters valid_output_formats default_output_format deprecated]} endpoint
        {:keys [required optional default_filters filters access_token_types responses]} details
        {:keys [pagination-descriptions filter-descriptions endpoint-descriptions sample-responses]} raw-content
        endpoint-id (str (to-id-str path) "-" (.toLowerCase (name method)))
        {:keys [introduction success-description see-also]} (get endpoint-descriptions (str "/" endpoint-id ".md") {})]
    (with-optional-keys
      {:id (keyword endpoint-id)
       :path (str "/" path)
       :api-path (str "/api/2/" path)
       :method method
       :name (fix-multimethod-name (:name endpoint) method)
       :description introduction
       :category {:section (first category) :api (second category)}
       :parameters (collect-parameters required optional pathParameters pagination-descriptions endpoint)
       :?pagination (collect-pagination-params optional pagination-descriptions)
       :?filters (map #(create-filter % default_filters filter-descriptions) filters)
       :response-formats (map keyword valid_output_formats)
       :default-response-format (keyword default_output_format)
       :access-token-types (set (map keyword access_token_types))
       :requires-authentication? (not (empty? access_token_types))
       :?relevant-endpoints (parse-see-also see-also)
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
