(ns clojure-game-api.core-test
  (:require [clojure.test :refer :all]
            [clojure-game-api.core :refer :all]))

(deftest makeMove_happyFlow
  (def board {:B2 :O :A1 :X})
  (def expected {:A1 :X :B2 :O :A2 :X})
  (testing "first board test"
    (is (= expected (makeMove board :A2, :X)))))

(deftest makeMove_error_outside
  (def board {:B2 :O :A1 :X})
  (testing "first board test"
           (is (thrown-with-msg? Exception #"invalid field :F19" (makeMove board :F19, :X)))))

(deftest makeMove_error_invalid_value
  (testing "values must be :X or :O"
           (is (thrown-with-msg? Exception #"invalid value :S" (makeMove initialBoard :A3, :S)))))

(deftest makeMove_duplicate_value
  (def board {:B2 :O :A1 :X})
  (testing "first board test"
           (is (thrown-with-msg? Exception #"field already set" (makeMove board :A1, :O)))))

; add test to validate state of the board, not only coordinates
(deftest boardIsValid
  (def board {:B2 :O :A1 :X})
  (testing "board is valid"
           (is (= true (validateBoard board)))))

(deftest boardHasInvalidKey
  (def board {:B2 :O :A1 :X, :G3 :O})
  (testing "board has invalid key"
           (is (= false (validateBoard board)))))

(deftest boardHasInvalidValue
  (def board {:B2 :O :A1 :Z})
  (testing "board has invalid value"
           (is (= false (validateBoard board)))))

(deftest boardHasUnbalancedValues
  (def board {:B2 :O :A1 :X :A2 :X :A3 :X})
  (testing "board is valid"
           (is (= false (validateBoard board))))
)

(deftest boardIsFull
 (def board {:A1 :X :A2 :O :A3 :X :B1 :O :B2 :X :B3 :O :C1 :X :C2 :O :C3 :X})
 (testing "board is full"
          (is (= false (validateBoardNotFull board)))))

(deftest boardIsNotFull
 (def board {:A2 :O :A3 :X :B1 :O :B2 :X :B3 :O :C1 :X :C2 :O :C3 :X})
 (testing "board is full"
          (is (= true (validateBoardNotFull board)))))

(deftest cpuOpponentRandomMoves_initBoard
   (def board (cpuOpponentRandomMoves initialBoard :X))
   (testing "move has been made"
            (is (= 1 (count (keys board))))
            (is (= #{:X} (into #{} (vals board))))))