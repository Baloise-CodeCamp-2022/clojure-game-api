(ns clojure-game-api.sudoku.generator
  (:require [clojure.set :as set]
            [clojure-game-api.sudoku.core :refer :all]
            )
  )

(defn setField [firstEmptyField board]
  (let [
        chosenNumber (first (firstEmptyField :POSSIS))
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
     (if (= initialFieldCount (count nonEmptyFields) )
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

(defn generate3 []
  (let [
        initial-board (generate3_initial emptyBoard 5)
        ;t0 (println initial-board)
        result (gsolveSudoku initial-board)
        ;t1 (println result)
        ]
    result
    )
  )


