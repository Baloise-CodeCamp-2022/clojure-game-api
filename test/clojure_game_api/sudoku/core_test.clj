(ns clojure-game-api.sudoku.core-test
    (:require [clojure.test :refer :all]
      [clojure-game-api.sudoku.core :refer :all]
      [clojure-game-api.sudoku.main :refer :all]
      [clojure.java.io :refer :all]
      ))

(deftest setNumber_HappyFlow
   (def expected [[0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 2 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0]])
   (testing "setting a number"
            (is (= expected (setNumber emptyBoard 3 2 2 )))))

(deftest validateBoard_HappyFlow
         (def board [[0 9 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0]
                        [0 0 0 2 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0]
                        [0 0 3 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0]
                        [0 4 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0]])
         (testing "validating a valid board"
                  (is (= nil (validateBoard board)))))

(deftest validateBoard_error_NumberTooLarge
         (def expected {:error true})
         (def board [[0 9 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 0 0 12 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 0 3 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 4 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]])
         (testing "validating a valid board"
                  (is (= expected (validateBoard board)))))

(deftest validateBoard_error_NumberTooSmall
         (def expected {:error true})
         (def board [[0 9 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 0 0 0 -1 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 0 3 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]
                     [0 4 0 0 0 0 0 0 0]
                     [0 0 0 0 0 0 0 0 0]])
         (testing "validating a valid board"
                  (is (= expected (validateBoard board)))))