(defproject spid-docs "0.1.0-SNAPSHOT"
  :description "SPiD API endpoint documentation"
  :url "https://payment.schibsted.no"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "2.1.0"]
                 [ring "1.2.1"]
                 [optimus "0.14.3"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [clygments "0.1.1"]
                 [me.raynes/cegdown "0.1.1"]
                 [mapdown "0.2.1"]
                 [spid-client-clojure "1.0.0"]
                 [commons-lang "2.5"]
                 [ring/ring-codec "1.0.0"]
                 [prismatic/schema "0.2.1"]
                 [fs "1.3.3"]
                 [clj-http "0.9.0"]
                 [inflections "0.9.5"]
                 [org.slf4j/slf4j-api "1.7.5"]
                 [ch.qos.logback/logback-classic "1.0.13"]
                 [com.google.code.gson/gson "2.2.4"]]
  :plugins [[lein-shell "0.3.0"]]
  :prep-tasks [["shell" "./update-example-repos.sh"]]
  :jvm-opts ["-Djava.awt.headless=true"
             "-Xmx1024M"]
  :test2junit-output-dir "target/test2junit"
  :resource-paths ["resources" "generated"]
  :ring {:handler spid-docs.web/app}
  :aliases {"build-site" ["run" "-m" "spid-docs.web/export"]
            "generate-sample-responses" ["run" "-m" "spid-docs.sample-responses.generate/generate"]
            "import-endpoints" ["run" "-m" "spid-docs.import-endpoints/import-endpoints"]}
  :profiles {:dev {:plugins [[lein-ring "0.8.10"]
                             [codox "0.6.7"]]
                   :dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :resource-paths ["dev/resources"]
                   :source-paths ["dev"]
                   :test-paths ^:replace []}
             :test {:dependencies [[midje "1.6.0"]
                                   [hiccup-find "0.3.0"]
                                   [test-with-files "0.1.0"]]
                    :resource-paths ["test/resources"]
                    :plugins [[lein-midje "3.1.3"]]}
             :basic {:prep-tasks ^:replace []}})
