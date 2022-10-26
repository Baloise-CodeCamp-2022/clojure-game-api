(ns clojure-game-api.sudoku.core)

(def emptyBoard
  (vec (repeat 9 (vec (repeat 9 0)))))

(defn setNumber [board x y n]
  (assoc board y (assoc (get board y) x n)))