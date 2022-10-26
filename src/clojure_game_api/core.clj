(ns clojure-game-api.core
  (:require [clojure.java.io :refer :all]
            [clojure.set :as set]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all])
  (:gen-class))

; ------------------- dependency injection --------------------------------
(def di-context (atom {}))
(defn di-put [key value]
  (swap! di-context assoc (keyword key) value))

(defn di-get [key]
  (get @di-context (keyword key) ))

; ------------------- TicTacToe --------------------------------
(def validCoordinates #{:A1, :A2, :A3, :B1, :B2, :B3, :C1, :C2, :C3})
(def validValues #{:X, :O})

(def GAME_IN_PROGRESS "IN PROGRESS")
(def GAME_WON "WON")
(def GAME_LOST "LOST")
(def GAME_DRAW "DRAW")

(def GAME_STATE (atom [GAME_IN_PROGRESS]))

(def validPlayers #{"human" "cpuOpponentRandomMoves"})

(def initialBoard {})

(defn checkForWin [board value]
  (let [stringOfFieldsWithValue (apply str (remove #{value} (sort (flatten ((group-by val board) value)))))
        abcResult (or (= 3 (count (re-seq #"A" stringOfFieldsWithValue)))
                      (= 3 (count (re-seq #"B" stringOfFieldsWithValue)))
                      (= 3 (count (re-seq #"C" stringOfFieldsWithValue)))
                      )
        onetwothreeResult (or (= 3 (count (re-seq #"1" stringOfFieldsWithValue)))
                              (= 3 (count (re-seq #"2" stringOfFieldsWithValue)))
                              (= 3 (count (re-seq #"3" stringOfFieldsWithValue)))
                              )
        diagonal1 (not (nil? (re-seq #":A1.*:B2.*:C3.*" stringOfFieldsWithValue)))
        diagonal2 (not (nil? (re-seq #":A3.*:B2.*:C1.*" stringOfFieldsWithValue)))]
    (or abcResult onetwothreeResult diagonal1 diagonal2))
  )

(defn validateBoardNotFull [board]
  (< (count (keys board)) 9))

(defn validateBalance [board]
  (if (= board initialBoard)
    true
    (let [freq (frequencies (vals board))
          numberOfOFields (get freq :O 0)
          numberOfXFields (get freq :X 0)]
      (< (abs (- numberOfOFields numberOfXFields)) 2))))

(defn validateBoard [board]
  (and (set/subset? (keys board) validCoordinates)
       (set/subset? (into #{} (vals board)) validValues)
       (validateBalance board)))

(defn makeMove [board coordinate value]
  (if (not (contains? validCoordinates coordinate))
    (throw (Exception. (str "invalid field " coordinate))))
  (if (not (contains? validValues value))
    (throw (Exception. (str "invalid value " value))))
  (if (contains? board coordinate)
    (throw (Exception. (str "field already set" coordinate))))

  (let [newBoard (conj board {coordinate value})]
    (if (not (validateBoard newBoard))
      (throw (Exception. (str "board is invalid" newBoard))))

    (let [gameStateUpdate
          (if (checkForWin newBoard value)
            (if (= value :X)                                ; should be humanPlayer
              GAME_WON
              GAME_LOST)
            (if (validateBoardNotFull board)
              GAME_IN_PROGRESS
              GAME_DRAW
              )
            )]
      (reset! GAME_STATE [gameStateUpdate])
      {:board newBoard :status gameStateUpdate}
      )))

; CPU player 1 - makes a random move and returns the board
(defn cpuOpponentRandomMoves [board value]
  (if (validateBoardNotFull board)
    (let [validMoves (set/difference validCoordinates (into #{} (keys board)))
          targetField (get (into [] validMoves) (rand-int (count validMoves)))]
      (makeMove board targetField value))
    {:board board :status GAME_DRAW}
    ))

; CPU player 2 - a bit more strategy
; Get all fields of the given field in its row
(defn tttRow [field]
  (let [letter (subs (name field) 0 1)]
    (set (map (fn [x] (keyword (str letter x))) '(1, 2, 3)))
    ))
(defn tttCol [field]
  (let [tttNum (subs (name field) 1)]
    (set (map (fn [x] (keyword (str x tttNum))) '("A", "B", "C")))
    ))
; how many tics does player have in the given fields
(defn ticCount [board fields player]
  (let [partOfBoard (select-keys board fields)]
    (count (filter (fn [e] (= (second e) player)) partOfBoard))))
; move gets a value of 10 if it prohibits a win of the opponent and 0 otherwise
(defn valueOfMove [board move player]
  ; tttRow board :A2 --> :A1 :A2 :A3
  ; tttCol
  ; tttDiag1 tttDiag2
  (let [other (if (= player :X) :O :X)
        rowTics (ticCount board (tttRow move) other)
        colTics (ticCount board (tttCol move) other)
        ; todo myDiag1/2
        ]
    (if (or (= rowTics 2) (= colTics 2)) 10 0)
    ))

(defn cpuOpponent2 [board player]
  (let [validMoves (set/difference validCoordinates (into #{} (keys board)))
        movesWithWeight (into {} (for [m validMoves] [m (valueOfMove board m player)]))
        targetField (first (last (sort-by second movesWithWeight)))]
    (makeMove board targetField player)
    ))

(defn stringMapToKeywordMap [inMap]
  (into {} (for [[k v] inMap] [k (keyword v)])))

