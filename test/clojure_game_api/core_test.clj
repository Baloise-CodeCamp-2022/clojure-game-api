(ns clojure-game-api.core-test
  (:require [clojure.test :refer :all]
            [clojure-game-api.core :refer :all]
            [clojure-game-api.main :refer :all]
            [clojure.java.io :refer :all]
            ))

(deftest makeMove_happyFlow
  (def board {:B2 :O :A1 :X})
  (def expected {:A1 :X :B2 :O :A2 :X})
  (testing "first board test"
    (is (= expected ((makeMove board :A2, :X) :board)))))

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
   (def board ((cpuOpponentRandomMoves initialBoard :X) :board))
   (testing "move has been made"
            (is (= 1 (count (keys board))))
            (is (= #{:X} (into #{} (vals board))))))

(deftest cpuOponent2_prohibitsWinner
  (def board {:A1 :X :A2 :X, :B1 :O})
  (testing "Oponent prohibits the other guy to win - has to put :O on A3"
    (let [res (cpuOpponent2 board :O)]
      (is (= ((res :board) :A3)) :O)
      )
    )
  )

(deftest checkForWin_initialBoard
  (testing "board is empty, no one won"
           (is (= false (checkForWin initialBoard :X) ))
  )
)

(deftest checkForWin_noWin
  (def board {:A2 :O :A3 :O :B1 :O :B2 :X :B3 :O :C1 :X :C2 :O :C3 :X})
  (testing "board is full, no one won"
           (is (= false (checkForWin board :X) ))
  )
)

(deftest checkForWin_WinOneDimension
  (def board {:A1 :X :A2 :X :A3 :X :B1 :O :B2 :X :B3 :O :C1 :X :C2 :O :C3 :X})
  (testing "board is full, X one won (A1-A3)"
           (is (= true (checkForWin board :X) ))
  )
)

(deftest checkForWin_WinOneDimension_2
  (def board {:A1 :O :A2 :O :A3 :O :B1 :O :B2 :O :B3 :O :C1 :O :C2 :O :C3 :X})
  (testing "board is full, O won (A2-C2)"
           (is (= true (checkForWin board :O)))
  )
)

(deftest checkForWin_WinDiagnoal_1
  (def board {:A1 :X :A2 :O :A3 :O :B1 :O :B2 :X :B3 :O :C1 :O :C2 :O :C3 :X})
  (testing "board is full, X won (A1-C3)"
           (is (= true (checkForWin board :X)))
  )
)

(deftest checkForWin_WinDiagnoal_2
  (def board {:A1 :X :A2 :O :A3 :O :B1 :O :B2 :O :B3 :O :C1 :O :C2 :O :C3 :X})
  (testing "board is full, O won (A3-C1)"
           (is (= true (checkForWin board :O)))
  )
)

(deftest stringMapToKeywordMap_test
  (def inMap {:A1 "X" :A2 "O"})
  (testing "stringMapToKeywordMap"
           (is (= {:A1 :X :A2 :O} (stringMapToKeywordMap inMap)))))

(deftest validateProgramArguments_HappyFlow
  (def args ["cpuOpponentRandomMoves" "human"])
  (def expectedArgs ["cpuOpponentRandomMoves" "human"])
  (testing "valid player names"
    (is (= expectedArgs (validateProgramArguments args)))
  )
)

(deftest validateProgramArguments_WrongPlayerName
  (def args ["cpuOpponentRandomMoves2" "human"])
  (def expectedArgs ["human" "cpuOpponentRandomMoves"])
  (testing "wrong name corrected"
    (is (= expectedArgs (validateProgramArguments args)))
  )
)

(deftest validateProgramArguments_WrongNumberOfElements
  (def args ["human"])
  (def expectedArgs ["human" "cpuOpponentRandomMoves"])
  (testing "missing element added"
    (is (= expectedArgs (validateProgramArguments args)))
  )
)

(deftest validateProgramArguments_WrongNumberOfElements_2
  (def args [])
  (def expectedArgs ["human" "cpuOpponentRandomMoves"])
  (testing "missing elements added"
    (is (= expectedArgs (validateProgramArguments args)))
  )
)
