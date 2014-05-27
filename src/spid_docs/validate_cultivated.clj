(ns spid-docs.validate-cultivated
  (:require [schema.core :refer [optional-key validate maybe enum pred eq either Str Num Keyword]]
            [spid-docs.validate-raw :refer [Type]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9/{}_\-.]+)+/?\*?$" s)) 'simple-slash-prefixed-path))

(def Parameter
  {:name Str
   (optional-key :aliases) [Str]
   :description (maybe Str)
   :type (enum :path :query)
   :required? Boolean})

(def ResponseFormat
  (enum :json :jsonp :png :xml :tgz :csv))

(def Filter
  {:name Str
   :description (maybe Str)
   :default? Boolean})

(def Response
  {(optional-key :status) Num
   (optional-key :description) Str
   :type (maybe (either Keyword [Keyword] {Keyword Keyword}))
   (optional-key :samples) {Keyword Str}})

(def Endpoint
  {:id Keyword
   :path Path
   :api-path Path
   :method (enum :GET :POST :DELETE)
   :name Str
   :description (maybe Str)
   :category {:section Str, :api Str}
   :parameters [Parameter]
   :response-formats [ResponseFormat]
   :default-response-format ResponseFormat
   (optional-key :pagination) [Parameter]
   (optional-key :filters) [Filter]
   :access-token-types #{(enum :user :server)}
   :requires-authentication? Boolean
   :example-params {Str Str}
   (optional-key :relevant-endpoints) [{:method Keyword :path Path}]
   (optional-key :relevant-types) [Str]
   (optional-key :relevant-articles) [{:title Str :path Path}]
   :responses {:success Response
               :failures [Response]}
   (optional-key :deprecated) Str})

(def Article
  {:title Str
   :body Str
   (optional-key :aside) Str
      (optional-key :frontpage) Str
   (optional-key :relevant-endpoints) [{:method Keyword :path Path}]})

(def Api
  {:api Str
   :category Str
   :endpoints [Endpoint]
   (optional-key :url) Str
   (optional-key :description) Str})

(defn validate-content [content]
  (validate {:endpoints [Endpoint]
             :articles {Str Article}
             :types {Keyword Type}
             :apis {[Str] Api}
             :endpoint-blacklist #{[(either Str Keyword)]}}
            content))
