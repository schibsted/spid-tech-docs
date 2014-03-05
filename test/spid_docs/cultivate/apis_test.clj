(ns spid-docs.cultivate.apis-test
  (:require [spid-docs.cultivate.apis :refer :all]
            [midje.sweet :refer :all]))

(fact "Adds endpoints to apis"
      (let [apis [{:id :identity-management
                   :title "Identity Management"
                   :apis [{:id :login :description "Retrieve login information"}
                          {:id :user :description "Create, modify and list user-specifc data"}]}
                  {:id :payment
                   :title "Payment"
                   :apis [{:id :product :description "Create, modify and list products"}
                          {:id :subscription :description "Create, modify and list subscriptions"}]}]
            endpoints [{:id :1 :categorization [[:identity-management :login]]}
                       {:id :2 :categorization [[:identity-management :user]]}
                       {:id :3 :categorization [[:payment :product]]}
                       {:id :4 :categorization [[:payment :subscription]]}
                       {:id :5 :categorization [[:identity-management :login]]}
                       {:id :6 :categorization [[:identity-management :user]]}
                       {:id :7 :categorization [[:payment :product]]}
                       {:id :8 :categorization [[:payment :user]]}]]
        (->> (cultivate-apis apis endpoints)
             first
             :apis
             (map #(map :id (:endpoints %))))) => '((:1 :5) (:2 :6)))
