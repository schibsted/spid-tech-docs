(ns spid-docs.example-code
  (:require [clojure.set :refer [difference]]
            [clojure.string :as str]
            [spid-docs.homeless :refer [update-vals]]))

(defn- get-example [match example-params]
  (or (example-params match)
      (throw (Exception. (str "No example found for parameter '" match "'. Add it to resources/example-params.edn")))))

(defn- replace-path-parameters [url example-params]
  (str/replace url #"\{([^}]+)\}" (fn [[_ match]] (get-example match example-params))))

(defn- exemplify-params [params example-params]
  (map #(vector (name (:name %)) (get-example (:name %) example-params)) params))

(defn- format-params [params tpl & [sep]]
  (str/join (or sep "") (map #(-> tpl
                                  (str/replace #":name" (first %))
                                  (str/replace #":value" (second %))) params)))

(defn curl-example-code [{:keys [api-path method access-token-types example-params]} params]
  (apply str "curl https://login.schibsted.com" (replace-path-parameters api-path example-params)
         (case method
           :GET " -G"
           :POST " \\\n   -X POST"
           :DELETE " -G \\\n   -X DELETE")
         (when (seq access-token-types) " \\\n   -H \"Authorization: Bearer [access token]\"")
         (format-params (exemplify-params params example-params)
                        " \\\n   -d \":name=:value\"")))

(defn- create-params-hash-map [params example-params]
  (when (seq params)
    (str "Map<String, String> params = new HashMap<>() {{\n    "
         (format-params (exemplify-params params example-params) "put(\":name\", \":value\");" ",\n    ")
         "\n}};\n\n")))

(defn- create-params-node-map [params example-params]
  (when (seq params)
    (str "  var params = {\n"
         (-> (format-params (exemplify-params params example-params) "    :name: ':value',\n")
             (str/replace #",\n$" ""))
         "\n  };\n")))

(defn- create-params-assoc-array [params example-params]
  (when (seq params)
    (str "$params = array(\n"
         (format-params (exemplify-params params example-params) "    \":name\" => \":value\"" ",\n")
         "\n);\n\n")))

(defn- create-examples-with [ex-fn endpoint req-params optional-params all-params]
  {:minimal (ex-fn endpoint req-params)
   :maximal (when (seq optional-params) (ex-fn endpoint all-params))})

(defn create-example-code [endpoint]
  (let [params (:parameters endpoint)
        all-params (filter #(= (:type %) :query) params)
        req-params (filter :required? all-params)
        optional-params (difference (set all-params) (set req-params))]
    (-> {:curl curl-example-code}
        (update-vals #(create-examples-with % endpoint req-params optional-params all-params)))))
