(ns spid-docs.content
  (:require [spid-docs.core :as spid]
            [stasis.core :as stasis]))

(defn load-content []
  {:endpoints (spid/get-endpoints)
   :articles (stasis/slurp-directory "resources/articles" #"\.md$")})
