(ns spid-docs.defsample
  (:import clojure.lang.Keyword
           clojure.lang.PersistentArrayMap
           clojure.lang.PersistentVector
           clojure.lang.Symbol
           java.lang.String))

(def sample-responses (atom []))

(defn- type-kw [thing]
  (cond
   (keyword? thing) :keyword
   (symbol? thing) :symbol
   (string? thing) :string
   (map? thing) :map
   (vector? thing) :vector
   :default :unknown))

(defn- get-deps [deps]
  (->> (partition 2 deps)
       (map #(vector (keyword (first %)) (second %)))
       (into {})))

(defn build-sample [forms]
  (case (mapv type-kw forms)
    [:symbol :string] {:method (keyword (first forms))
                       :path (second forms)}
    [:symbol :string :map] {:method (keyword (first forms))
                            :path (second forms)
                            :params (nth forms 2)}
    [:keyword :symbol :string] {:id (first forms)
                                :method (keyword (second forms))
                                :path (nth forms 2)}
    [:keyword :symbol :string :map] {:id (first forms)
                                     :method (keyword (second forms))
                                     :path (nth forms 2)
                                     :params (nth forms 3)}
    [:string :symbol :string] {:docstring (first forms)
                               :method (keyword (second forms))
                               :path (nth forms 2)}
    [:string :symbol :string :map] {:docstring (first forms)
                                    :method (keyword (second forms))
                                    :path (nth forms 2)
                                    :params (nth forms 3)}
    [:vector :symbol :string] {:dependencies (get-deps (first forms))
                               :method (keyword (second forms))
                               :path (nth forms 2)}
    [:vector :symbol :string :map] {:dependencies (get-deps (first forms))
                                    :method (keyword (second forms))
                                    :path (nth forms 2)
                                    :params (nth forms 3)}
    [:keyword :string :symbol :string] {:id (first forms)
                                        :docstring (second forms)
                                        :method (keyword (nth forms 2))
                                        :path (nth forms 3)}
    [:keyword :string :symbol :string :map] {:id (first forms)
                                             :docstring (second forms)
                                             :method (keyword (nth forms 2))
                                             :path (nth forms 3)
                                             :params (nth forms 4)}
    [:keyword :vector :symbol :string] {:id (first forms)
                                        :dependencies (get-deps (second forms))
                                        :method (keyword (nth forms 2))
                                        :path (nth forms 3)}
    [:keyword :vector :symbol :string :map] {:id (first forms)
                                             :dependencies (get-deps (second forms))
                                             :method (keyword (nth forms 2))
                                             :path (nth forms 3)
                                             :params (nth forms 4)}
    [:vector :string :symbol :string] {:dependencies (get-deps (first forms))
                                       :docstring (second forms)
                                       :method (keyword (nth forms 2))
                                       :path (nth forms 3)}
    [:vector :string :symbol :string :map] {:dependencies (get-deps (first forms))
                                            :docstring (second forms)
                                            :method (keyword (nth forms 2))
                                            :path (nth forms 3)
                                            :params (nth forms 4)}
    [:keyword :vector :string :symbol :string] {:id (first forms)
                                                :dependencies (get-deps (second forms))
                                                :docstring (nth forms 2)
                                                :method (keyword (nth forms 3))
                                                :path (nth forms 4)}
    [:keyword :vector :string :symbol :string :map] {:id (first forms)
                                                     :dependencies (get-deps (second forms))
                                                     :docstring (nth forms 2)
                                                     :method (keyword (nth forms 3))
                                                     :path (nth forms 4)
                                                     :params (nth forms 5)}))

(defn define-sample [forms]
  (swap! sample-responses #(conj % (build-sample forms))))

(defmacro defsample [& forms]
  (define-sample forms))
