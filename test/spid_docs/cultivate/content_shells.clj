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
    :defaultFilters []
    :accessTokenTypes []}
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
    :defaultOutputFormat "!"
    :validOutputFormats []
    :httpMethods {:GET (http-method)}
    :parameterDescriptions {}}
   m))
