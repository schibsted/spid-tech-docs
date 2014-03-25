(ns spid-docs.validate-raw
  (:require [schema.core :refer [optional-key validate either eq Str Num Keyword]]))

(def HttpMethod
  {:name Str
   :optional [Str]
   :required [Str]
   :responses [{:status Num
                :description Str
                :type Str}]
   :filters [Str]
   :default_filters [Str]
   :access_token_types [Str]})

(def Endpoint
  {:category [Str]
   :name Str
   :description Str
   :path Str
   :pathParameters [Str]
   :method Str
   :url Str
   :controller Str
   :default_output_format Str
   :valid_output_formats [Str]
   :httpMethods {(optional-key :GET) HttpMethod
                 (optional-key :POST) HttpMethod
                 (optional-key :DELETE) HttpMethod}
   :parameter_descriptions {Keyword Str}
   (optional-key :alias) {Keyword Str}
   (optional-key :deprecated) Str})

(def PrimitiveType
  {:id Keyword
   (optional-key :name) Str
   (optional-key :description) Str})

(def ObjectType
  (merge PrimitiveType
         {:rendering (eq :object)
          :fields [{:name Str
                    :type (either Keyword [Keyword])
                    (optional-key :description) Str
                    (optional-key :always-available?) Boolean}]}))

(def EnumType
  (merge PrimitiveType
         {:rendering (eq :enum)
          :values [{:value Str
                    :description Str}]}))

(def Type
  (either ObjectType EnumType PrimitiveType))

(def EndpointDescription
  {:description Str
   (optional-key :inline-types) Str
   (optional-key :success-description) Str})

(defn validate-raw-content [raw-content]
  (validate {:endpoints [Endpoint]
             :articles {Str Str}
             :concepts {Str Str}
             :sample-responses {Str Str}
             :endpoint-descriptions {Str EndpointDescription}
             :pagination-descriptions {Keyword Str}
             :filter-descriptions {Keyword Str}
             :types {Keyword Type}
             :apis {[Str] {Keyword Str}}} ; tuple of [:section :api] -> {:description ""}
            raw-content))
