(ns spid-docs.validate
  (:require [schema.core :refer [optional-key validate either Str Keyword]]))

(def HttpMethod
  {:name Str
   :required [Str]
   :optional [Str]})

(def Endpoint
  {:path Str
   :valid_output_formats [Str]
   :method Str
   :name Str
   (optional-key :alias) {:id Str}
   :httpMethods {(optional-key :GET) HttpMethod
                 (optional-key :POST) HttpMethod
                 (optional-key :DELETE) HttpMethod}
   :url Str
   :pathParameters [Str]
   :controller Str
   :default_output_format Str
   :description Str})

(def Type
  {:id Keyword
   (optional-key :description) Str
   (optional-key :name) Str
   (optional-key :type) Keyword
   (optional-key :fields) [{:name Str
                            :type (either Keyword [Keyword])
                            (optional-key :description) Str
                            (optional-key :always-available?) Boolean}]
   (optional-key :values) [{:value Str
                            :description Str}]})

(def APICategory
  {:id Keyword
   :title Str
   :apis [{:id Keyword
           :title Str
           :description Str}]})

(defn validate-raw-content [raw-content]
  (validate {:endpoints [Endpoint]
             :articles {Str Str}
             :concepts {Str Str}
             :params {Str Keyword}
             :types {Keyword Type}
             :apis [APICategory]}
            raw-content))
