(ns spid-docs.bindings)

(defn create-local-namespace
  "Import data from some global namespace into a local namespace. The bindings
   specify which members to include in the new local namespace.

   Example:

   (create-local-namespace {:johndoe {:id 1}
                            :janedoe {:id 2}} {'user :janedoe})
   ;;=>
   {'user {:id 2}}"
  [global-ns bindings]
  (zipmap (keys bindings) (map global-ns (vals bindings))))

(defn inject-bindings
  "Injects data from a namespace map into the expression. The expression can be
   any Clojure expression. bindings is a map of {Symbol Any}. The symbols may be
   used in the expression, and will be replaced by the corresponding values.

   Example:

   (inject-bindings {'person {:name \"John\"}}
                     [42 (:name person)])
   ;;=>
   [42 \"John\"]"
  [expr ns]
  (let [inject-recursively #(inject-bindings % ns)]
    (cond
     ;; When the expression is a symbol, first check if we have a binding for
     ;; this symbol. If we do, replace the symbol with the corresponding data.
     ;; Otherwise, leave the symbol untouched
     (symbol? expr) (if (contains? ns expr) (expr ns) expr)

     ;; When the expression is a map, recursively inject dependencies in the map
     ;; values. (Keys are left untouched)
     (map? expr) (zipmap (keys expr) (map inject-recursively (vals expr)))

     ;; Seqs and vectors are handled recursively.
     (seq? expr) (map inject-recursively expr)
     (vector? expr) (mapv inject-recursively expr)

     ;; Other expressions are left untouched
     :else expr)))

(defn resolve-bindings
  "Injects data from a namespace map into the expression using the provided
   bindings. The expression can be any Clojure expression. bindings is a map of
   {Symbol Keyword}. The symbols may be used in the expression, and the keywords
   map to keys in the namespace map.

   Symbols will be replaced with the :response :data from the defs map for
   matching bindings.

   Example:

   (resolve-bindings [42 (:name person)]
                     {'person :john}
                     {:john {:name \"John\"}})
   ;;=>
   [42 \"John\"]

   See tests for further examples"
  [expr bindings ns]
  (inject-bindings expr (create-local-namespace ns bindings)))
