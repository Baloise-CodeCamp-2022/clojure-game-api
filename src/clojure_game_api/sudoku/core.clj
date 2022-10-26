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
(defn getNumber [board x y]
  (get (get board y) x))

(def easy
  [
   [0 6 0 3 0 0 8 0 4]
   [5 3 7 0 9 0 0 0 0]
   [0 4 0 0 0 6 3 0 7]
   [0 9 0 0 5 1 2 3 8]
   [0 0 0 0 0 0 0 0 0]
   [7 1 3 6 2 0 0 4 0]
   [3 0 6 4 0 0 0 1 0]
   [0 0 0 0 6 0 5 2 3]
   [1 0 2 0 0 9 0 8 0]
   ]
  )

(defn sCol [board x]
  (map (fn [y] (get (get board y) x)) (range 9)))
(defn sRow [board y]
  (get board y))