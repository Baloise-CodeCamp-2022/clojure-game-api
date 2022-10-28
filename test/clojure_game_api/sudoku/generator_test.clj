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
   (def expected [[8 0 0 0 0 0 0 0 0]
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
  (def expected [[8 2 4 5 9 1 3 6 7]
                 [5 3 1 8 6 4 9 2 0]
                 [9 6 7 2 3 0 8 5 4]
                 [2 8 0 9 5 7 6 3 1]
                 [3 5 0 6 8 0 2 9 0]
                 [6 9 0 3 2 0 5 8 0]
                 [4 0 8 1 7 5 0 0 9]
                 [1 0 5 4 0 8 7 0 2]
                 [7 0 9 0 0 2 4 1 8]])
  (testing "generate_2"
    (is (= (generate2 emptyBoard) expected))))

(deftest generate_3a
  (def expected [[8 0 0 0 0 0 0 0 0]
                 [5 0 0 0 0 0 0 0 0]
                 [9 0 0 0 0 0 0 0 0]
                 [2 0 0 0 0 0 0 0 0]
                 [3 0 0 0 0 0 0 0 0]
                 [6 0 0 0 0 0 0 0 0]
                 [4 0 0 0 0 0 0 0 0]
                 [1 0 0 0 0 0 0 0 0]
                 [7 0 0 0 0 0 0 0 0]])
  (testing "generate_3a"
    (is (= (generate3a emptyBoard) expected))))

(deftest generate_3
  (def expected [[8 7 6 1 4 3 2 9 5]
                 [5 1 3 9 6 2 4 8 7]
                 [9 4 2 7 5 8 6 1 3]
                 [2 6 1 5 3 7 9 4 8]
                 [3 9 4 6 8 1 7 5 2]
                 [7 5 8 4 2 9 3 6 1]
                 [1 3 7 8 9 6 5 2 4]
                 [4 2 9 3 1 5 8 7 6]
                 [6 8 5 2 7 4 1 3 9]])
  (testing "generate_3"
    (is (= (generate3 5) expected))))

(deftest generate_4_5
  (def expected [[0 7 6 0 4 3 0 9 5]
                 [5 0 3 9 0 2 4 0 7]
                 [9 4 0 7 5 0 6 1 0]
                 [0 6 1 0 3 7 0 4 8]
                 [3 0 4 6 0 1 7 0 2]
                 [7 5 0 4 2 0 3 6 0]
                 [0 3 7 0 9 6 0 2 4]
                 [4 0 9 3 0 5 8 0 6]
                 [6 8 0 2 7 0 1 3 0]])
  (testing "generate_4-5"
    (is (= (generate4 5) expected))))

(deftest generate_4_12
  (def expected [[0 2 7 0 9 6 0 3 5]
                 [5 0 1 4 0 8 7 0 9]
                 [9 6 0 7 5 0 2 8 0]
                 [0 7 9 0 8 5 0 4 3]
                 [3 0 5 9 0 7 8 0 6]
                 [6 4 0 3 1 0 5 9 0]
                 [0 9 6 0 7 1 0 5 8]
                 [1 0 3 8 0 4 9 0 2]
                 [7 8 0 5 3 0 6 1 0]])
  (testing "generate_4-12"
    (is (= (generate4 12) expected))))

(deftest generate_x
  (testing "generate_4"
    (is (not (= (generate4 4) (generate4 5))) )))

