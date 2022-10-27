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
         (testing "validating an invalid board"
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
         (testing "validating an invalid board"
                  (is (= expected (validateBoard board)))))

(deftest validatesSetNumber_HappyFlow
         (testing "validating a valid setNumber execution"
                  (is (= nil (validateSetNumber emptyBoard 0 0 1)))))

(deftest validatesSetNumber_WrongXCoordinate
         (testing "validating an invalid setNumber execution/x not in range"
                  (is (= error (validateSetNumber emptyBoard 40 0 1)))))

(deftest validatesSetNumber_WrongYCoordinate
         (testing "validating an invalid setNumber execution/y not in range"
                  (is (= error (validateSetNumber emptyBoard 0 -34 1)))))

(deftest validatesSetNumber_WrongNumber
         (testing "validating an invalid setNumber execution/n not in range"
                  (is (= error (validateSetNumber emptyBoard 0 0 98)))))

(deftest validateSudoSolverEasyBoard
         (def expected [[2 6 1 3 7 5 8 9 4]
                        [5 3 7 8 9 4 1 6 2]
                        [9 4 8 2 1 6 3 5 7]
                        [6 9 4 7 5 1 2 3 8]
                        [8 2 5 9 4 3 6 7 1]
                        [7 1 3 6 2 8 9 4 5]
                        [3 5 6 4 8 2 7 1 9]
                        [4 8 9 1 6 7 5 2 3]
                        [1 7 2 5 3 9 4 8 6]])
         (testing ""
                  (is (= expected (solveSudoku easyBoard))))
)