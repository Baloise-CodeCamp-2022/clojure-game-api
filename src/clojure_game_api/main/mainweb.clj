(ns clojure-game-api.main.mainweb
  (:require
    [clojure-game-api.main.maincore :refer :all]
    [clojure.java.io :refer :all]
    [compojure.core :refer :all]
    [org.httpkit.server :as server]
    [ring.middleware.defaults :refer :all]
    [ring.middleware.file :refer :all]
    [ring.middleware.resource :refer :all]
    )
  (:gen-class))

(defn start-web-server [routes]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))
        routeHandler (wrap-defaults routes api-defaults)
        ]
    ; Run the server with Ring.defaults middleware
    (server/run-server (-> routeHandler
                           (wrap-resource "public")) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/")))
  )
