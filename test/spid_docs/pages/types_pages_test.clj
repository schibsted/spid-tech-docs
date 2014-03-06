(ns spid-docs.pages.types-pages-test
  (:require [spid-docs.pages.type-pages :refer :all]
            [midje.sweet :refer :all]))

(fact "It generates type paths only for types with descriptions"
      (type-path {:id :string}) => nil
      (type-path {:id :datetime :description "ISO-8601"}) => "/types/datetime")

(fact "It creates pages for types with description"
      (let [pages (create-pages [{:id :string}
                                 {:id :datetime
                                  :description "ISO-8601"}
                                 {:id :integer}])]
        ((pages "/types/datetime")) => {:body (list [:h1 "datetime"]
                                                    "<p>ISO-8601</p>")}))
