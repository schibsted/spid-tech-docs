(defproject spid-docs "0.1.0-SNAPSHOT"
  :description "SPiD API endpoint documentation"
  :url "https://payment.schibsted.no"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "1.0.0"]
                 [ring "1.2.1"]
                 [optimus "0.14.2"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [clygments "0.1.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [spid-sdk-clojure "0.2.0"]
                 [commons-lang "2.5"]
                 [ring/ring-codec "1.0.0"]
                 [prismatic/schema "0.2.0"]
                 [fs "1.3.3"]
                 [clj-http "0.9.0"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :ring {:handler spid-docs.web/app}
  :aliases {"build-site" ["run" "-m" "spid-docs.web/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.8.10"]
                             [codox "0.6.7"]]
                   :dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.6.0"]
                                   [hiccup-find "0.3.0"]]
                    :plugins [[lein-midje "3.1.3"]]}})
