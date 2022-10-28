(ns clojure-game-api.sudoku.generator
  (:require [clojure.set :as set]
            [clojure-game-api.sudoku.core :refer :all]
            )
  )

(defn setField [firstEmptyField board]
  (let [
        chosenNumber (last (firstEmptyField :POSSIS))
        x (firstEmptyField :X)
        y (firstEmptyField :Y)
        result (setNumber board x y chosenNumber)
        ]
    result
    ))

(defn generate2 [board]
  (let [
        fieldInfos (allPossis board)
        emptyFields (filter (fn [x] (= (x :CURRENT) 0)) fieldInfos)
        emptyFieldsWithPossis (filter (fn [f] (not ( empty? (f :POSSIS)))) emptyFields)
        firstEmptyField (first emptyFieldsWithPossis)
        result (if (empty? emptyFieldsWithPossis) board (setField firstEmptyField board) )
        ]
     (if (empty? emptyFieldsWithPossis) result (generate2 result) )
    )
  )

; generate board with an given number of initialized fields
(defn generate3_initial [board initialFieldCount]
  (let [
        fieldInfos (allPossis board)
        emptyFields (filter (fn [x] (= (x :CURRENT) 0)) fieldInfos)
        nonEmptyFields (filter (fn [x] (not (= (x :CURRENT) 0)) ) fieldInfos)
        emptyFieldsWithPossis (filter (fn [f] (not ( empty? (f :POSSIS)))) emptyFields)
        firstEmptyField (first emptyFieldsWithPossis)
        result (if (empty? emptyFieldsWithPossis) board (setField firstEmptyField board) )
        ]
     (if (= (- initialFieldCount 1) (count nonEmptyFields) )
       result
       (generate3_initial result initialFieldCount) )
    )
  )

(defn generate3a [board]
  (generate3_initial board 9)
  )

(declare gsolveSudoku)
(defn gtryPossis [board possis]
  (let [trials (filter #(some? %)
                       (for [n (possis :POSSIS)]
                         (gsolveSudoku (setNumber board (possis :X) (possis :Y) n))))
        ]
    (if (empty? trials)
      nil
      (first trials)
      )))

(defn gsolveSudoku [board]
  (let [allPossis (sort-by #(count (% :POSSIS)) (filter #(= (% :CURRENT) 0) (allPossis board)))]
    (if (empty? allPossis)
      board
      (gtryPossis board (first allPossis))
      )
    )
  )

(defn generate3 [initial-number-count]
  (let [
        initial-board (generate3_initial emptyBoard initial-number-count)
        t0 (println initial-board)
        result (gsolveSudoku initial-board)
        ;t1 (println result)
        ]
    result
    )
  )


(defn generate4 [initial-number-count]
  (let [
        initial-board (generate3_initial emptyBoard initial-number-count)
        solvedBoard (gsolveSudoku initial-board)
        userBoard (-> solvedBoard
                     (resetCell 0 0)
                     (resetCell 1 1)
                     (resetCell 2 2)
                     (resetCell 3 0)
                     (resetCell 4 1)
                     (resetCell 5 2)
                     (resetCell 6 0)
                     (resetCell 7 1)
                     (resetCell 8 2)

                     (resetCell 0 3)
                     (resetCell 1 4)
                     (resetCell 2 5)
                     (resetCell 3 3)
                     (resetCell 4 4)
                     (resetCell 5 5)
                     (resetCell 6 3)
                     (resetCell 7 4)
                     (resetCell 8 5)

                     (resetCell 0 6)
                     (resetCell 1 7)
                     (resetCell 2 8)
                     (resetCell 3 6)
                     (resetCell 4 7)
                     (resetCell 5 8)
                     (resetCell 6 6)
                     (resetCell 7 7)
                     (resetCell 8 8)

            )
        solvedBoard2 (gsolveSudoku userBoard)
        ]
    (println initial-board)
    (println solvedBoard)
    (println userBoard)
    (println solvedBoard2)
    userBoard
    )
  )


