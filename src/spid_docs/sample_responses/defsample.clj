(ns spid-docs.sample-responses.defsample
  (:import clojure.lang.Keyword
           clojure.lang.PersistentArrayMap
           clojure.lang.PersistentVector
           clojure.lang.Symbol
           java.lang.String)
  (:require [clojure.string :as str]
            [spid-docs.api-client :as api]
            [spid-docs.homeless :refer [update-existing]]))

(def sample-responses
  "Using the defsample macro will store sample response definitions in this
   vector. It may be reset elsewhere, but beware that resetting it runtime will
   clear the results of any uses of the macro, as the macro is evaluated at read
   time. This means that linear-looking code like this:

   (defsample ...)
   (reset! sample-responses [])
   (defsample ...)

   Will leave the sample-responses vector empty, not with one item. This happens
   because the macros are evaluated first, during read, and the reset call is
   evaluated later, during evaluation."
  (atom []))

(defn- type-kw
  "Returns the type of its argument as a keyword"
  [thing]
  (cond
   (keyword? thing) :keyword
   (symbol? thing) :symbol
   (string? thing) :string
   (map? thing) :map
   (vector? thing) :vector
   :default :unknown))

(defn- get-deps
  "Takes a map of dependencies {Symbol Symbol} and turns it into a map of
  {Symbol Keyword} This map can then be used for bindings with the inject-deps
  function."
  [deps]
  (->> (partition 2 deps)
       (map #(vector (first %) (keyword (second %))))
       (into {})))

(defn- get-query-params
  "Given a path with placeholders like {this} and a map of parameters, return a
  map of path parameters (e.g. only the params keys that are used as
  placeholders in the URL)"
  [path params]
  (->> (re-seq #"\{(.*?)\}" path)  ; Find all {placeholders} in the path
       (map (comp keyword second)) ; Each result is ["{name}" "name"] - keywordize the second element
       (select-keys params)))      ; Return a map with only the keys from the params map that were in the path

(defn- build-sample-def
  "Takes a map of raw data and turns it into a sample response definition map"
  [sample]
  (let [path-params (get-query-params (:path sample) (:params sample))]
    (-> (if (seq path-params)
          (assoc sample :path-params path-params)
          sample)
        (update-existing
         [:id] keyword
         [:method] keyword
         [:dependencies] get-deps
         [:params] #(apply dissoc % (keys path-params))))))

(defn build-sample
  "This is the main engine behind the defsample macro. It accepts various
   combinations of arguments and returns a sample response definition map."
  [forms]
  (build-sample-def
   (case (mapv type-kw forms)
     ;; (defsample GET "/path")
     [:symbol :string]
     {:method (nth forms 0)
      :path   (nth forms 1)}

     ;; (defsample GET "/users/{userId}" {:userId 42})
     [:symbol :string :map]
     {:method (nth forms 0)
      :path   (nth forms 1)
      :params (nth forms 2)}

     ;; (defsample john
     ;;   GET "/user/13")
     [:symbol :symbol :string]
     {:id     (nth forms 0)
      :method (nth forms 1)
      :path   (nth forms 2)}

     ;; (defsample john
     ;;   GET "/user/{userId}" {:userId 42})
     [:symbol :symbol :string :map]
     {:id     (nth forms 0)
      :method (nth forms 1)
      :path   (nth forms 2)
      :params (nth forms 3)}

     ;; (defsample
     ;;   "A docstring"
     ;;   GET "/user/42")
     [:string :symbol :string]
     {:docstring (nth forms 0)
      :method    (nth forms 1)
      :path      (nth forms 2)}

     ;; (defsample
     ;;   "A docstring"
     ;;   GET "/user/{userId}" {:userId 42})
     [:string :symbol :string :map]
     {:docstring (nth forms 0)
      :method    (nth forms 1)
      :path      (nth forms 2)
      :params    (nth forms 3)}

     ;; (defsample [user john]
     ;;   GET "/user/42")
     [:vector :symbol :string]
     {:dependencies (nth forms 0)
      :method       (nth forms 1)
      :path         (nth forms 2)}

     ;; (defsample [user john]
     ;;   GET "/user/{userId}" {userId (:id user)})
     [:vector :symbol :string :map]
     {:dependencies (nth forms 0)
      :method       (nth forms 1)
      :path         (nth forms 2)
      :params       (nth forms 3)}

     ;; (defsample jane
     ;;   "A docstring"
     ;;   GET "/user/42")
     [:symbol :string :symbol :string]
     {:id        (nth forms 0)
      :docstring (nth forms 1)
      :method    (nth forms 2)
      :path      (nth forms 3)}

     ;; (defsample jane
     ;;   "A docstring"
     ;;   GET "/user/{userId}" {userId 42})
     [:symbol :string :symbol :string :map]
     {:id        (nth forms 0)
      :docstring (nth forms 1)
      :method    (nth forms 2)
      :path      (nth forms 3)
      :params    (nth forms 4)}

     ;; (defsample jane [user john]
     ;;   GET "/user/42")
     [:symbol :vector :symbol :string]
     {:id           (nth forms 0)
      :dependencies (nth forms 1)
      :method       (nth forms 2)
      :path         (nth forms 3)}

     ;; (defsample jane [user john]
     ;;   GET "/user/{userId}" {userId (:id user)})
     [:symbol :vector :symbol :string :map]
     {:id           (nth forms 0)
      :dependencies (nth forms 1)
      :method       (nth forms 2)
      :path         (nth forms 3)
      :params       (nth forms 4)}

     ;; (defsample [user john]
     ;;   "A docstring"
     ;;   GET "/user/42")
     [:vector :string :symbol :string]
     {:dependencies (nth forms 0)
      :docstring    (nth forms 1)
      :method       (nth forms 2)
      :path         (nth forms 3)}

     ;; (defsample [user john]
     ;;   "A docstring"
     ;;   GET "/user/{userId}" {userId (:id user)})
     [:vector :string :symbol :string :map]
     {:dependencies (nth forms 0)
      :docstring    (nth forms 1)
      :method       (nth forms 2)
      :path         (nth forms 3)
      :params       (nth forms 4)}

     ;; (defsample jane [user john]
     ;;   "A docstring"
     ;;   GET "/user/42")
     [:symbol :vector :string :symbol :string]
     {:id           (nth forms 0)
      :dependencies (nth forms 1)
      :docstring    (nth forms 2)
      :method       (nth forms 3)
      :path         (nth forms 4)}

     ;; (defsample jane [user john]
     ;;   "A docstring"
     ;;   GET "/user/{userId}" {userId (:id user)})
     [:symbol :vector :string :symbol :string :map]
     {:id           (nth forms 0)
      :dependencies (nth forms 1)
      :docstring    (nth forms 2)
      :method       (nth forms 3)
      :path         (nth forms 4)
      :params       (nth forms 5)})))

(defn define-sample
  "Implementation of the defsample macro. It accepts a vector of arguments
   (see the macro), creates a sample response definition, and adds it to the
   sample-responses vector."
  [forms]
  (swap! sample-responses #(conj % (build-sample forms))))

(defmacro defsample
  "Create a sample response definition map and store it in sample-responses.

   The macro has the following signature (question mark indicating an optional
   argument):

   (defsample name? dependencies? docstring? method path params?)

   name         is a symbol
   dependencies is a map of {Symbol Symbol}
   docstring    is a string
   method       is a symbol
   path         is a string
   params       is a map. It may use keys from dependencies as bindings in
                its values (even in nested expressions)"
  [& forms]
  (define-sample forms))
