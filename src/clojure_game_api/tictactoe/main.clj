(ns clojure-game-api.tictactoe.main
  (:require [clojure.java.io :refer :all]
            [clojure.set :as set]
            [clojure-game-api.main.maincore :refer :all]
            [clojure-game-api.tictactoe.core :refer :all]
            [clojure-game-api.tictactoe.web :refer :all]
            [clojure-game-api.tictactoe.persistence.memory :refer :all]
            [compojure.core :refer :all]
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

(def tictactoe-di-context {
                         :save #'saveBoard
                         :load #'loadBoard
                         }
  )

(defn tictactoe
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

  ;(def validProgramArguments (validateProgramArguments args))
  )

