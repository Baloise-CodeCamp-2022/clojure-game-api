(ns clojure-game-api.sudoku.core)

(def emptyBoard
  (vec (repeat 9 (vec (repeat 9 0)))))

(defn validateBoard [board]
  (let [erroneousNumbers (filter #(or (> % 9) (< % 0)) (flatten board))]
    (if (not (empty? erroneousNumbers))
      {:error true}
    )))

(defn setNumber [board x y n]
  (assoc board y (assoc (get board y) x n)))