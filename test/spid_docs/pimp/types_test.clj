(ns spid-docs.pimp.types-test
  (:require [spid-docs.pimp.types :refer :all]
            [midje.sweet :refer :all]))

(fact
 "It creates a list with client types and a check mark for specified clients using spid-client tags"

 (create-types (str "<spid-types>WA</spid-types>"))
 => (str "<ul class=\"types-list\">"
           "<li><div class=\"type-radio\"><span class=\"check\"> ✓</span></div>Web</li>"
           "<li><div class=\"type-radio\"><span class=\"check\"></span></div>Mobile</li>"
           "<li><div class=\"type-radio\"><span class=\"check\"></span></div>Backend</li>"
           "<li><div class=\"type-radio\"><span class=\"check\"> ✓</span></div>Admin</li>"
           "</ul>"))
