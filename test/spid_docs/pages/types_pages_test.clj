(ns spid-docs.pages.types-pages-test
  (:require [hiccup-find.core :refer [hiccup-find hiccup-text]]
            [midje.sweet :refer :all]
            [spid-docs.pages.type-pages :refer :all]))

(fact "It creates pages for types with description"
      (let [pages (create-pages {:string {:id :string}
                                 :datetime {:id :datetime
                                            :description "ISO-8601"}
                                 :integer {:id :integer}})]
        ((pages "/types/datetime")) => {:body [:div.wrap
                                               [:h1 "datetime"]
                                               "<p>ISO-8601</p>"]}))

(let [login {:id :login
             :name "Login"
             :description "A login object"
             :fields [{:name "id" :type :string}
                      {:name "emails" :type [:string]}]}
      user {:id :user
            :name "User"
            :description "A user person"
            :fields [{:name "created" :type :datetime}]}
      types {:login login
             :user user
             :datetime {:id :datetime :name "Datetime" :description "Dates and stuff"}
             :string {:id :string :name "String"}
             :login-type {:id :login-type
                          :name "Login type"
                          :description "Something"}}]

  (fact "Renders type definitions"
        (->> (render-type-definition login types)
             (hiccup-find [:table.sectioned])
             count) => 1)

  (fact "Links to non-inline types with description"
        (->> (render-type-definition user types)
             (hiccup-find [:h5 :a])
             first
             second ; Attributes
             :href) => "/types/datetime")

  (fact "Distinguishes lists of types"
        (->> (render-type-definition login types)
             (hiccup-find [:h5])
             (map hiccup-text)) => ["string" "list of strings"]))
