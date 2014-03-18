(ns spid-docs.validate-cultivated
  (:require [schema.core :refer [optional-key validate enum pred eq Str Num Keyword]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9/{}\-.]+)+/?$" s)) 'simple-slash-prefixed-path))

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
        {:section "Data Storage"        :api "SODA Endpoints"}
        {:section "Data Storage"        :api "DataObjects API"}
        {:section "Data Storage"        :api "Traits API"}
        {:section "Insight"             :api "Report Database API"}
        {:section "Insight"             :api "KPI API"}
        {:section "Authorization"       :api "OAuth"}
        {:section "Utilities"           :api "Platform API"}))

(def Parameter
  {:name Str
   (optional-key :aliases) [Str]
   :description Str
   :type (enum :path :query)
   :required? Boolean})

(def ResponseFormat
  (enum :json :jsonp :png :xml :tgz :csv))

(def Filter
  {:name Str
   :description Str
   :default? Boolean})

(def Response
  {:status Num
   :description Str
   :type Keyword})

(def Endpoint
  {:id Str
   :path Path
   :api-path Path
   :method (enum :GET :POST :DELETE)
   :name Str
   :category APICategory
   :parameters [Parameter]
   :response-formats [ResponseFormat]
   :default-response-format ResponseFormat
   (optional-key :pagination) [Parameter]
   (optional-key :filters) [Filter]
   :access-token-types [(enum :user :server)]
   :requires-authentication? Boolean
   :responses {:success Response
               :failure [Response]}
   (optional-key :deprecated) Str})
