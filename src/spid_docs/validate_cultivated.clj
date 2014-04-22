(ns spid-docs.validate-cultivated
  (:require [schema.core :refer [optional-key validate maybe enum pred eq either Str Num Keyword]]
            [spid-docs.validate-raw :refer [Type]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9/{}_\-.]+)+/?\*?$" s)) 'simple-slash-prefixed-path))

(def APICategory
  (enum {:section "Identity Management" :api "Login API"}
        {:section "Identity Management" :api "User API"}
        {:section "Identity Management" :api "Email API"}
        {:section "Payment Services"    :api "Product API"}
        {:section "Payment Services"    :api "Subscription API"}
        {:section "Payment Services"    :api "Bundle API"}
        {:section "Payment Services"    :api "Digital Contents API"}
        {:section "Payment Services"    :api "Payment API"}
        {:section "Payment Services"    :api "Direct Payment API"}
        {:section "Payment Services"    :api "Order API"}
        {:section "Payment Services"    :api "Campaign API"}
        {:section "Payment Services"    :api "Voucher API"}
        {:section "Payment Services"    :api "PayLink API"}
        {:section "Payment Services"    :api "InjectToken API"}
        {:section "Payment Services"    :api "Discount API"}
        {:section "Data Storage"        :api "SODA Endpoints"}
        {:section "Data Storage"        :api "DataObjects API"}
        {:section "Data Storage"        :api "Traits API"}
        {:section "Insight"             :api "Report Database API"}
        {:section "Insight"             :api "KPI API"}
        {:section "Authorization"       :api "OAuth"}
        {:section "Utilities"           :api "Platform API"}
        {:section "Utilities"           :api "Sysadmin API"}))

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
   :category APICategory
   :parameters [Parameter]
   :response-formats [ResponseFormat]
   :default-response-format ResponseFormat
   (optional-key :pagination) [Parameter]
   (optional-key :filters) [Filter]
   :access-token-types #{(enum :user :server)}
   :requires-authentication? Boolean
   (optional-key :relevant-endpoints) [{:method Keyword :path Path}]
   :responses {:success Response
               :failures [Response]}
   (optional-key :deprecated) Str})

(def Article
  {:title Str
   :body Str
   (optional-key :aside) Str
   (optional-key :relevant-endpoints) [{:method Keyword :path Path}]})

(defn validate-content [content]
  (validate {:endpoints [Endpoint]
             :articles {Str Article}
             :concepts {Str Str}
             :types {Keyword Type}
             :apis {[Str] {:api Str
                           :category Str
                           :endpoints [Endpoint]
                           (optional-key :description) Str}}
             :endpoint-blacklist #{[(either Str Keyword)]}}
            content))
