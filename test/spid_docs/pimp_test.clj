(ns spid-docs.pimp-test
  (:require [midje.sweet :refer :all]
            [spid-docs.enlive :refer [select parse]]
            [spid-docs.pimp :refer :all]))

(fact "Adds anchors to all headings"
      (->> (add-header-anchors "<div>
<h2>Poblano</h2>
<p>Mild and earthy</p>
<h2>Hungarian Hot Wax</h2>
<p>Great for pickling</p>")
           parse
           (select [:h2 :a])
           (map :attrs)) => [{:name "poblano"} {:name "hungarian-hot-wax"}])
