(ns spid-docs.pages.type-pages-test
  (:require [hiccup-find.core :refer [hiccup-find hiccup-text]]
            [midje.sweet :refer :all]
            [spid-docs.pages.type-pages :refer :all]))

(fact "It creates pages for types with description"
      (let [pages (create-pages {:string {:id :string}
                                 :datetime {:id :datetime
                                            :description "ISO-8601"}
                                 :integer {:id :integer}})]
        (->> ((pages "/types/datetime"))
             :body
             (hiccup-find [:h1])
             first) => [:h1 "datetime"]))

(let [login {:id :login
             :name "Login"
             :description "A login object"
             :fields [{:name "id" :type :string}
                      {:name "emails" :type [:string]}]}
      user {:id :user
            :name "User"
            :description "A user person"
            :fields [{:name "created" :type :datetime}]}
      status {:id :status
              :name "Status"
              :rendering :enum
              :values [{:value "0" :description "Active"}]}
      product {:id :product
               :rendering :object
               :inline-types [:status]
               :fields [{:name "id" :type :integer}
                        {:name "status" :type :status}]}
      types {:login login
             :user user
             :status status
             :datetime {:id :datetime :name "Datetime" :description "Dates and stuff"}
             :string {:id :string :name "string"}
             :login-type {:id :login-type
                          :name "Login type"
                          :description "Something"}}]

  (fact "Renders type definitions"
        (->> (render-type-definition login types)
             (hiccup-find [:table.sectioned])
             count) => 1)

  (fact "Renders enum types"
        (->> (render-type-definition status types)
             (hiccup-find [:table.sectioned :tr])
             count) => 1)

  (fact "Links to non-inline types with description"
        (->> (render-type-definition user types)
             (hiccup-find [:h5 :a])
             first
             second ; Attributes
             :href) => "/types/datetime")

  (fact "Links to non-inline types with fields"
        (->> (-> types
                 (assoc-in [:datetime :description] nil)
                 (assoc-in [:datetime :fields] [{}]))
             (render-type-definition user)
             (hiccup-find [:h5 :a])
             first
             second ; Attributes
             :href) => "/types/datetime")

  (fact "Links to non-inline types with fields"
        (->> (render-type-definition user (-> types
                                              (assoc-in [:datetime :description] nil)
                                              (assoc-in [:datetime :fields] [{}])))
             (hiccup-find [:h5 :a])
             first
             second ; Attributes
             :href) => "/types/datetime")

  (fact "Distinguishes lists and collections of types"
        (->> (render-type-definition login types)
             (hiccup-find [:h5])
             (map hiccup-text)) => ["string" "list of strings"])

  (fact "Renders inline types"
        (->> (create-page product types)
             :body
             (hiccup-find [:h3])) => '([:h3 {:id "status"} "Status"])))

(fact "Links to types"
      (link-to-type :string {:string {:id :string}}) => "string"

      (link-to-type :string {:string
                             {:id :string
                              :description "Descriptions earns the type a separate page"}
                             }) => [:a {:href "/types/string"} "string"]

      (link-to-type :string {:string
                             {:id :string-thing
                              :name "string thing"
                              :inline? true
                              :description "Descriptions earns the type a separate page"}
                             }) => [:a {:href "#string-thing"} "string thing"]

      (link-to-type :string {:string
                             {:id :string
                              :name "lol strings"
                              :description "Links to name if present"}
                             }) => [:a {:href "/types/string"} "lol strings"]

      (link-to-type :something {:something
                                {:id :something
                                 :fields [{:name "field" :description "earns the type a separate page"}]}
                                }) => [:a {:href "/types/something"} "something"]

      (link-to-type :something {:something
                                {:id :something
                                 :values [{:value "field" :description "earns the type a separate page"}]}
                                }) => [:a {:href "/types/something"} "something"]

      (link-to-type [:string] {:string
                                  {:id :string}}) => '("list of " "strings")

      (link-to-type [:something] {:something
                                  {:id :something :description "OK"}
                                  }) => '("list of " [:a {:href "/types/something"} "somethings"])

      (link-to-type {:userId :user} {:user
                                     {:id :user :description "OK"}
                                     }) => '("collection of "
                                             [:a {:href "/types/user"} "users"]
                                             ", as an object with "
                                             [:code "userId"]
                                               " for property names, and "
                                             [:a {:href "/types/user"} "users"]
                                             " for values")

      (link-to-type {:userId :user} {:user
                                     {:id :user}
                                     }) => '("collection of "
                                             "users"
                                             ", as an object with "
                                             [:code "userId"]
                                             " for property names, and "
                                             "users"
                                             " for values"))
