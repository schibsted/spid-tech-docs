(ns spid-docs.defsample
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

(defn- interpolate [string data]
  (str/replace string #"\{(.*)\}" (fn [[_ var]] (str ((keyword var) data)))))

(defn inject-deps
  "Takes a map of samples and injects them into the expression. The expression
   can be any Clojure expression. bindings is a map of {Symbol Keyword}. The
   symbols may be used in the expression, and the keywords map to keys in the
   sample defs map.

   Symbols will be replaced with the :response :data from the defs map for
   matching bindings.

   Example:

   (inject-deps {:john {:response {:data {:name \"John\"}}}}
                {'person :john}
                [42 person])
   ;;=>
   [42 \"John\"]

   See tests for further examples"
  [defs bindings expr]
  (let [inject-recursively (partial inject-deps defs bindings)]
    (cond
     ;; When the expression is a symbol, first check if we have a binding for this
     ;; symbol. If we do, check if the binding maps to a sample definition. If it
     ;; does, replace the symbol with the corresponding sample definition's
     ;; :response :data. Otherwise, leave the symbol untouched
     (symbol? expr) (if (and (contains? bindings expr)
                             (contains? defs (bindings expr)))
                      (-> ((bindings expr) defs) :response :data)
                      expr)

     ;; When the expression is a map, recursively inject dependencies in the map
     ;; values. (Keys are left untouched)
     (map? expr) (zipmap (keys expr) (map inject-recursively (vals expr)))

     ;; Seqs and vectors are handled recursively.
     (seq? expr) (map inject-recursively expr)
     (vector? expr) (mapv inject-recursively expr)

     ;; Other expressions are left untouched
     :else expr)))

(defn interpolate-sample-def
  "Injects dependencies and interpolates path parameters. Returns an updated
   sample definition map."
  [def & [defs]]
  (let [defs-map (zipmap (map :id defs) defs)
        inject-dependencies (comp eval (partial inject-deps defs-map (:dependencies def)))
        dep-injected (-> def
                         (update-existing [:params] inject-dependencies)
                         (update-existing [:path-params] inject-dependencies))]
    (update-in dep-injected [:path] #(interpolate % (:path-params dep-injected)))))

(defn fetch-sample-response
  "Fetch sample response from the API"
  [sample-def]
  (let [method (:method sample-def)
        path (:path sample-def)
        params (:params sample-def)
        response (cond
                  (= method :GET) (api/raw-GET path params)
                  (= method :POST) (api/raw-POST path params)
                  (= method :DELETE) (api/raw-DELETE path params))]
    {:method method
     :path (:path sample-def)
     :response {:status (:status response)
                :error (:error response)
                :data (:data response)
                :success? (:success? response)}}))
