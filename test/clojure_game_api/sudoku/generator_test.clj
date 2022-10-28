(ns clojure-game-api.sudoku.generator-test
    (:require [clojure.test :refer :all]
      [clojure-game-api.sudoku.core :refer :all]
      [clojure-game-api.sudoku.generator :refer :all]
      [clojure.java.io :refer :all]
      ))

; Just for development
;(deftest possis_1
;   (def expected [[0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]
;                  [0 0 0 0 0 0 0 0 0]])
;   (testing "possis_1"
;            (is (= (allPossis emptyBoard) expected))))

(deftest setField_1
   (def expected [[7 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]])
   (def fieldInfos (allPossis emptyBoard))
   (def firstEmptyField (first (filter (fn [x] (= (x :CURRENT) 0)) fieldInfos)))
   (testing "setField_1"
            (is (= (setField firstEmptyField expected) expected))))

(deftest generate_1
   (def expected [[7 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]])
   (testing "generate_1"
            (is (= (generate1 emptyBoard) expected))))

