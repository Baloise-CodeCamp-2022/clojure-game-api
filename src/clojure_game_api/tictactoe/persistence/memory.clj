(ns clojure-game-api.tictactoe.persistence.memory
  (:require [clojure.java.io :refer :all]
            [clojure-game-api.tictactoe.core :refer :all]
            [clojure-game-api.tictactoe.web :refer :all]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all])
  (:gen-class))

(def boardRepository (atom {}))

(defn saveBoard [boardName board]
  (let [
        newBoard (swap! boardRepository assoc (keyword boardName) board)
        ]
    (println boardRepository)
    newBoard
    )
  )

; save to file:
;(spit (str "/tmp/" name ".json") board)

(defn loadBoard [boardName]
  (get @boardRepository (keyword boardName) ))


