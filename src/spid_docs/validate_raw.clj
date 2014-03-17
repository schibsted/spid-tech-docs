(ns spid-docs.validate-raw
  (:require [schema.core :refer [optional-key validate either Str Num Keyword]]))

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
  {(optional-key :deprecated) Str
   :category [Str Str]
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
   :alias {Keyword Str}})
