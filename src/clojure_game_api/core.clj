(ns clojure-game-api.core
    (:require [org.httpkit.server :as server]
              [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.middleware.defaults :refer :all]
              [clojure.pprint :as pp]
              [clojure.string :as str]
              [clojure.data.json :as json])
    (:gen-class))
; ------------------- TicTacToe --------------------------------
(def tictactoe-board (atom []))
(defn makeMove [coordinate value]
  (swap! tictactoe-board conj {coordinate value}))
(makeMove :A1 true)
(makeMove :B2 true)
(defn tictactoe-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @tictactoe-board))})

; ------------------- App --------------------------------
(defroutes app-routes
  (GET "/tictactoe" [] tictactoe-handler)
  (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
