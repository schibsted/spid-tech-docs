(defproject spid-docs "0.1.0-SNAPSHOT"
  :description "SPiD API endpoint documentation"
  :url "https://payment.schibsted.no"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "0.7.1"]
                 [ring "1.2.1"]
                 [optimus "0.14.2"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [clygments "0.1.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [spid-sdk-clojure "0.1.0"]]
  :ring {:handler spid-docs.web/app}
  :aliases {"build-site" ["run" "-m" "spid-docs.web/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.8.10"]]
                   :source-paths ["dev"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.6.0"]]
                    :plugins [[lein-midje "3.1.3"]]}})
