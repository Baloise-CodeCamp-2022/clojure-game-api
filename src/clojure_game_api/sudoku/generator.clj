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
    (println chosenNumber)
    result
    ))

(defn generate1 [board]
  (let [
        fieldInfos (allPossis board)
        firstEmptyField (first (filter (fn [x] (= (x :CURRENT) 0)) fieldInfos))
        result (setField firstEmptyField board)
        ]
     (println firstEmptyField)
     ;(println result)
     result
    )
  )


