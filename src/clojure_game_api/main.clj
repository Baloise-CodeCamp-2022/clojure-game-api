(ns clojure-game-api.main
  (:require [clojure.java.io :refer :all]
            [clojure.set :as set]
            [clojure-game-api.core :refer :all]
            [clojure-game-api.web :refer :all]
            [compojure.core :refer :all]
            [org.httpkit.server :as server]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all])
  (:gen-class))

(defn validateProgramArguments [args]
    (def defaultProgramArguments ["human" "cpuOpponentRandomMoves"])
    (if-not (= (count args) 2)
        defaultProgramArguments
        (if (not (set/subset? (into #{} args) validPlayers))
            defaultProgramArguments
            args
        )
    )
)

(defn -main
  "This is our main entry point"
  ; args =  ["<first player>" "<second player>"]
  ; Can be:
  ;  ["human" "human"] - no CPU player, only the game board will be provided by the application
  ;  ["human" "cpuOpponentRandomMoves"] - Human player starts, the opponent is "cpuOpponentRandomMoves" (default)
  ;  ["human" "cpuOpponentBetterMoves"] - Human player starts, the opponent is a different CPU player.
  ;  ["cpuOpponentRandomMoves" "human"] - CPU player starts.
  ;  ["cpuOpponentRandomMoves" "cpuOpponentBetterMoves"] - Two CPU players, no user interaction.
  ;
  ; valid players are provided in the set validPlayers
  [& args]


  (def validProgramArguments (validateProgramArguments args))

  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))
        routeHandler (wrap-defaults #'app-routes api-defaults)
        ]
    ; Run the server with Ring.defaults middleware
    (server/run-server (-> routeHandler
                           (wrap-resource "public")) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
