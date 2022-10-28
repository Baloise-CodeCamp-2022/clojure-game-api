(ns clojure-game-api.sudoku.core
  (:require [clojure.set :as set])
  )

(def emptyBoard
  (vec (repeat 9 (vec (repeat 9 0)))))

(def board9
  ; source https://github.com/dimitri/sudoku/blob/master/sudoku.txt Grid09
  [
   [0 0 0 9 0 0 0 0 2]
   [0 5 0 1 2 3 4 0 0]
   [0 3 0 0 0 0 1 6 0]
   [9 0 8 0 0 0 0 0 0]
   [0 7 0 0 0 0 0 9 0]
   [0 0 0 0 0 0 2 0 5]
   [0 9 1 0 0 0 0 5 0]
   [0 0 7 4 3 9 0 2 0]
   [4 0 0 0 0 7 0 0 0]
   ]
  )

(def easyBoard
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

(def error
  {:error true})

(defn validateBoard [board]
  (let [erroneousNumbers (filter #(or (> % 9) (< % 0)) (flatten board))]
    (if (not (empty? erroneousNumbers))
      error
      )))

(defn validateSetNumber [board x y n]
  (if (or (> x 9) (< x 0))
    error
    (if (or (> y 9) (< y 0))
      error
      (if (or (> n 9) (< n 1))
        error
        (validateBoard board)
        )
      )))


(defn setNumber [board x y n]
  (let [validationCheck (validateSetNumber board x y n)]
    (if (= nil validationCheck)
      (assoc board y (assoc (get board y) x n))
      validationCheck)))

(defn getNumber [board x y]
  (get (get board y) x))


(defn sCol [board x]
  (map (fn [y] (get (get board y) x)) (range 9)))
(defn sRow [board y]
  (get board y))
(defn sSquare [board x y]
  "Get the square a positon x,y"
  (let [i (quot x 3)
        j (quot y 3)
        startX (* i 3)
        endX (+ startX 3)
        startY (* j 3)
        endY (+ startY 3)
        ]
    (for [u (range startX endX)
          v (range startY endY)]
      (getNumber board u v)
      )
    )
  )

(defn possis [board x y]
  (let [allNums (into #{} (range 1 10))
        existingNumbers
        (into #{}
              (flatten (conj (sCol board x)
                             (sRow board y)
                             (sSquare board x y)
                             )))]
    (set/difference allNums existingNumbers)
    )
  )

(defn allPossis [board]
  "Get a list of all possibilities for each field"
  (for [x (range 9)
        y (range 9)]
    {
     :X       x
     :Y       y
     :CURRENT (getNumber board x y)
     :POSSIS  (possis board x y)
     }
    ))

(declare solveSudoku)
(defn tryPossis [board possis]
  (let [trials (filter #(some? %)
                       (for [n (possis :POSSIS)]
                         (solveSudoku (setNumber board (possis :X) (possis :Y) n))))]
    (if (empty? trials)
      nil
      (first trials)
      )))

(defn solveSudoku [board]
  (let [allPossis (sort-by #(count (% :POSSIS)) (filter #(= (% :CURRENT) 0) (allPossis board)))]
    (if (empty? allPossis)
      board
      (tryPossis board (first allPossis))
      )
    )
  )