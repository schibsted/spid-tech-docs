(ns spid-docs.pimp.clickable-headings-test
  (:require [midje.sweet :refer :all]
            [spid-docs.enlive :refer [select parse]]
            [spid-docs.pimp.clickable-headings :refer :all]))

(fact "Adds anchors to all headings"
      (->> (make-headings-clickable "<div>
<h2>Poblano</h2>
<p>Mild and earthy</p>
<h2>Hungarian Hot Wax</h2>
<p>Great for pickling</p>")
           parse
           (select [:h2])) => [{:tag :h2
                                :attrs {:id "poblano"}
                                :content [{:tag :a
                                           :attrs {:class "anchor-link"
                                                   :href "#poblano"}
                                           :content ["Poblano"]}]}
                               {:tag :h2
                                :attrs {:id "hungarian-hot-wax"}
                                :content [{:tag :a
                                           :attrs {:class "anchor-link"
                                                   :href "#hungarian-hot-wax"}
                                           :content ["Hungarian Hot Wax"]}]}])
