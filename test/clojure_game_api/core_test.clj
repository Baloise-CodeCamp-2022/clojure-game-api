(ns clojure-game-api.core-test
  (:require [clojure.test :refer :all]
            [clojure-game-api.core :refer :all]))

(deftest a-test
  (testing "first simple test"
    (is (= 1 1))))

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
           (is (thrown-with-msg? Exception #"invalid value :S" (makeMove initalBoard :A3, :S)))))

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

