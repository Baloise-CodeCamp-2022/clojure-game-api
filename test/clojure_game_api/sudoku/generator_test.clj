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

(deftest generate_2
  (def expected [[7 6 9 1 4 5 3 2 8]
                 [1 3 5 7 2 9 4 6 0]
                 [4 2 8 6 3 0 7 1 9]
                 [6 7 0 4 1 8 2 3 5]
                 [3 1 0 2 7 0 6 4 0]
                 [2 4 0 3 6 0 1 7 0]
                 [9 0 7 5 8 1 0 0 4]
                 [5 0 1 9 0 7 8 0 6]
                 [8 0 4 0 0 6 9 5 7]])
  (testing "generate_2"
    (is (= (generate2 emptyBoard) expected))))

(deftest generate_3
  (def expected [[7 6 0 0 0 0 0 0 0]
                 [1 0 0 0 0 0 0 0 0]
                 [4 0 0 0 0 0 0 0 0]
                 [6 0 0 0 0 0 0 0 0]
                 [3 0 0 0 0 0 0 0 0]
                 [2 0 0 0 0 0 0 0 0]
                 [9 0 0 0 0 0 0 0 0]
                 [5 0 0 0 0 0 0 0 0]
                 [8 0 0 0 0 0 0 0 0]])
  (testing "generate_3"
    (is (= (generate3 emptyBoard 9) expected))))

