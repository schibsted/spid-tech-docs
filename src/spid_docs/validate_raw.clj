(ns spid-docs.validate-raw
  (:require [schema.core :refer [optional-key validate either eq Str Num Keyword]])
  (:import clojure.lang.IExceptionInfo))

(def HttpMethod
  {:name Str
   :optional [Str]
   :required [Str]
   :responses [{:status Num
                :description Str
                :type Str}]
   :filters [Str]
   :defaultFilters [Str]
   :accessTokenTypes [Str]})

(def Endpoint
  {:category [Str]
   :name Str
   :description Str
   :path Str
   :pathParameters [Str]
   :method Str
   :url Str
   :controller Str
   :defaultOutputFormat Str
   :validOutputFormats [Str]
   :httpMethods {(optional-key :GET) HttpMethod
                 (optional-key :POST) HttpMethod
                 (optional-key :DELETE) HttpMethod}
   :parameterDescriptions {Keyword Str}
   (optional-key :alias) {Keyword Str}
   (optional-key :deprecated) Str})

(def PrimitiveType
  {:id Keyword
   (optional-key :name) Str
   (optional-key :description) Str
   (optional-key :inline-types) [Keyword]})

(def ObjectType
  (merge PrimitiveType
         {:fields [{:name Str
                    :type (either Keyword [Keyword] {Keyword Keyword})
                    (optional-key :description) Str
                    (optional-key :always-available?) Boolean}]}))

(def EnumType
  (merge PrimitiveType
         {:values [{:value Str
                    :description Str}]}))

(def Type
  (either ObjectType EnumType PrimitiveType))

(def EndpointDescription
  {:introduction Str
   (optional-key :success-description) Str
   (optional-key :relevant-endpoints) Str
   (optional-key :relevant-types) Str
   (optional-key :example-params) Str})

(def ArticleFragment
  {:body Str
   (optional-key :title) Str
   (optional-key :heading) Str
   (optional-key :aside) Str
   (optional-key :frontpage) Str
   (optional-key :relevant-endpoints) Str})

(defn validate-raw-content [raw-content]
  (validate {:endpoints {Str Endpoint}
             :articles {Str (either ArticleFragment [ArticleFragment])}
             :sample-responses {Str Str}
             :endpoint-descriptions {Str EndpointDescription}
             :filter-descriptions {Keyword Str}
             :types {Keyword Type}
             :example-params {Str Str}
             :endpoint-blacklist #{[(either Str Keyword)]}}
            (update-in raw-content [:endpoints]
                       #(into {} (map (juxt :path identity) %))))
  raw-content)
