(ns spid-docs.enlive-test
  (:require [spid-docs.enlive :refer :all]
            [midje.sweet :refer :all]))

(fact "Parses strings to trees"
      (parse "<h1>Hey</h1>") => (list {:tag :h1 :attrs nil :content '("Hey")}))

(fact "Parses hiccup vectors to trees"
      (parse [:h1 "Hey"]) => (list {:tag :h1 :attrs nil :content '("Hey")}))

(fact "Parses a list of hiccup vectors to trees"
      (parse (list [:h1 "Hey"]
                   [:p "There"])) => (list {:tag :h1 :attrs nil :content '("Hey")}
                                           {:tag :p :attrs nil :content '("There")}))

(fact "Selects nodes in tree"
      (->> (list {:tag :h1 :attrs nil :content '("Hey")})
          (select [:h1])
          first
          :content
          first) => "Hey")

(fact "Transforms tree"
      (-> (list {:tag :h1 :attrs nil :content '("Hey")})
          (transform [:h1] #(assoc % :content "WAT"))) => "<h1>WAT</h1>")
