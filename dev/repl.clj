(ns repl
  (:require [spid-docs.web :refer [app]]
            [ring.adapter.jetty]))

(defn start-server []
  (defonce server (ring.adapter.jetty/run-jetty #'app {:port 3000 :join? false}))
  (.start server))

(defn stop-server []
  (.stop server))
