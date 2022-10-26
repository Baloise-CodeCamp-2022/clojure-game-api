(ns clojure-game-api.sudoku.web
  (:require [clojure-game-api.main.maincore :refer :all]
            [clojure-game-api.tictactoe.core :refer :all]
            [clojure.java.io :refer :all]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all])
  (:gen-class))


(def sudoku-handlers '(
  (GET "/sudoku/:id" [id] (str "<h1>Hello user " id "</h1>"))
  ))

; ------------------- App --------------------------------
;(defroutes sudoku-routes)

