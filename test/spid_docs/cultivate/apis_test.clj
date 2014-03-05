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

(fact "Sorts services by endpoint count"
      (let [apis [{:id :payment
                   :title "Payment"
                   :apis [{:id :product :description "Create, modify and list products" :endpoints [{}]}
                          {:id :blabla :description "Placeholder" :endpoints [{}]}
                          {:id :subscription :description "Create, modify and list subscriptions" :endpoints [{}]}]}
                  {:id :identity-management
                   :title "Identity Management"
                   :apis [{:id :login :description "Retrieve login information"
                           :endpoints [{} {}]}
                          {:id :user :description "Create, modify and list user-specifc data"
                           :endpoints [{} {} {}]}]}]]
        (->> (sort by-endpoint-count apis)
             (map :id)) => '(:payment :identity-management)))

(fact "Columnizes services"
      (let [apis [{:id :payment
                   :title "Payment"
                   :apis [{:id :product :description "Create, modify and list products" :endpoints [{}]}
                          {:id :blabla :description "Placeholder" :endpoints [{}]}
                          {:id :subscription :description "Create, modify and list subscriptions" :endpoints [{}]}]}
                  {:id :identity-management
                   :title "Identity Management"
                   :apis [{:id :login :description "Retrieve login information"
                           :endpoints [{} {}]}
                          {:id :user :description "Create, modify and list user-specifc data"
                           :endpoints [{} {} {}]}]}]
            columns (columnize apis 2)]
        (count columns) => 2
        (:id (get-in columns [0 0])) => :payment))
