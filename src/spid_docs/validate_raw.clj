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
   :name Str
   :rendering :primitive
   (optional-key :description) Str
   :inline-type? Boolean}) ; if true, should be rendered inline on endpoint page

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

(defn validate-raw-content [raw-content]
  (validate {:endpoints [Endpoint]
             :articles {Str Str}
             :concepts {Str Str}
             :params {Str Keyword}
             :types {Keyword Type}
             :apis {[Str Str] Str}} ; tuple of [:section :api] -> :description
            raw-content))
