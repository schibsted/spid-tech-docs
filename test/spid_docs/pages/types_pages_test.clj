(ns spid-docs.pages.types-pages-test
  (:require [spid-docs.pages.type-pages :refer :all]
            [midje.sweet :refer :all]))

(fact "It creates pages for types with description"
      (let [pages (create-pages {:string {:id :string}
                                 :datetime {:id :datetime
                                            :description "ISO-8601"}
                                 :integer {:id :integer}})]
        ((pages "/types/datetime")) => {:body [:div.wrap
                                               [:h1 "datetime"]
                                               "<p>ISO-8601</p>"]}))
