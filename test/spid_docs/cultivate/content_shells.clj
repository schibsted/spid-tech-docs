(ns spid-docs.cultivate.content-shells)

(defn http-method [& [m]]
  (merge
   {:name "!"
    :optional []
    :required []
    :responses [{:status 200
                 :description "!"
                 :type "!"}]
    :filters []
    :default_filters []
    :access_token_types []}
   m))

(defn endpoint [& [m]]
  (merge
   {:category ["!" "!"]
    :name "!"
    :description "!"
    :path "!"
    :pathParameters []
    :method "!"
    :url "!"
    :controller "!"
    :default_output_format "!"
    :valid_output_formats []
    :httpMethods {:GET (http-method)}
    :parameter_descriptions {}}
   m))
