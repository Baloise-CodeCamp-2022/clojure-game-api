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
(defn generate3 [board initialFieldCount]
  (let [
        fieldInfos (allPossis board)
        emptyFields (filter (fn [x] (= (x :CURRENT) 0)) fieldInfos)
        nonEmptyFields (filter (fn [x] (not (= (x :CURRENT) 0)) ) fieldInfos)
        emptyFieldsWithPossis (filter (fn [f] (not ( empty? (f :POSSIS)))) emptyFields)
        firstEmptyField (first emptyFieldsWithPossis)
        result (if (empty? emptyFieldsWithPossis) board (setField firstEmptyField board) )
        ]
     (if (= initialFieldCount (count nonEmptyFields) ) result (generate3 result initialFieldCount) )
    )
  )


