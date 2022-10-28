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
        emptyFieldsWithoutPossis (filter (fn [f] ( empty? (f :POSSIS))) emptyFields)
        firstEmptyField (first emptyFieldsWithPossis)
        ;t1 (println firstEmptyField)
        ;t2 (println emptyFieldsWithoutPossis)
        result (if (empty? emptyFieldsWithPossis) board (setField firstEmptyField board) )
        ]
     (if (empty? emptyFieldsWithPossis) result (generate2 result) )
    )
  )


