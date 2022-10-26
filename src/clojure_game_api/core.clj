(ns clojure-game-api.core
  (:require [clojure.data.json :as json]
            [clojure.set :as set]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all]
            [clojure.java.io :refer :all]
            [ring.util.response :as resp])
  (:gen-class))

; ------------------- TicTacToe --------------------------------
(def boardRepository (atom {}))

(def validCoordinates #{:A1, :A2, :A3, :B1, :B2, :B3, :C1, :C2, :C3})
(def validValues #{:X, :O})



(def GAME_IN_PROGRESS "IN PROGRESS")
(def GAME_WON "WON")
(def GAME_LOST "LOST")
(def GAME_DRAW "DRAW")

(def validPlayers #{"human" "cpuOpponentRandomMoves"})

(def initialBoard {})

(defn checkForWin [board value]
  (def stringOfFieldsWithValue (apply str (remove #{value} (sort (flatten ((group-by val board) value))))))
  (def abcResult
    (or (= 3 (count (re-seq #"A" stringOfFieldsWithValue)))
        (= 3 (count (re-seq #"B" stringOfFieldsWithValue)))
        (= 3 (count (re-seq #"C" stringOfFieldsWithValue)))
        )
    )

  (def onetwothreeResult
    (or (= 3 (count (re-seq #"1" stringOfFieldsWithValue)))
        (= 3 (count (re-seq #"2" stringOfFieldsWithValue)))
        (= 3 (count (re-seq #"3" stringOfFieldsWithValue)))
        )
    )

  (def diagonal1 (not (nil? (re-seq #":A1.*:B2.*:C3.*" stringOfFieldsWithValue))))
  (def diagonal2 (not (nil? (re-seq #":A3.*:B2.*:C1.*" stringOfFieldsWithValue))))

  (or abcResult onetwothreeResult diagonal1 diagonal2)
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

  (def newBoard (conj board {coordinate value}))

  (if (not (validateBoard newBoard))
    (throw (Exception. (str "board is invalid" newBoard))))

  (def gameStateUpdate
    (if (checkForWin newBoard value)
        (if (= value :X) ; should be humanPlayer
            GAME_WON
            GAME_LOST)
        (if (validateBoardNotFull board)
            GAME_IN_PROGRESS
            GAME_DRAW
        )
    )
  )
  {:board newBoard :status gameStateUpdate}
)

; CPU player 1 - makes a random move and returns the board
(defn cpuOpponentRandomMoves [board value]
   (if (validateBoardNotFull board)
   (do
       (def validMoves (set/difference validCoordinates (into #{} (keys board))))
       (def targetField (get (into [] validMoves) (rand-int (count validMoves))))
       (makeMove board targetField value)
   )
   {:board board :status GAME_DRAW}
  )
)

(defn saveBoard [boardName board]
  (swap! boardRepository assoc (keyword boardName) board))

(defn loadBoard [boardName]
  (get @boardRepository (keyword boardName) ))

(defn stringMapToKeywordMap [inMap]
  (into {} (for [[k v] inMap] [k (keyword v)])))

(defn handle-new-move [request]
  (let [board (stringMapToKeywordMap (get-in request [:body :board]))
        afterCallerMove (makeMove board
                                  (keyword (get-in request [:body :move :field])),
                                  (keyword (get-in request [:body :move :value])))
        returnBoardAndStatus (if (or (= GAME_WON (afterCallerMove :status)) (= GAME_LOST (afterCallerMove :status)))
                               afterCallerMove
                               (cpuOpponentRandomMoves (afterCallerMove :board) :O)) ;should be cpuPlayer
        ]

    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (str (json/write-str returnBoardAndStatus))})
  )

(use
  '[ring.middleware.json :only [wrap-json-body]]
  '[ring.util.response :only [response]])
(def handle-new-move-json (wrap-json-body handle-new-move {:keywords? true}))

; ----------------------------------
(defn handle-save [request]
  (println "SAVE:")
  (let [
        name (-> request :params :name)
        board (stringMapToKeywordMap (get-in request [:body :board]))
        result board
        ]
    (println name)
    (saveBoard name board)
    (println boardRepository)
    (println (loadBoard name))
    ; save to file:
    ;(spit (str "/tmp/" name ".json") board)
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (str (json/write-str result))})
  )

(defn handle-save-json [request]
  (wrap-json-body handle-save {:keywords? true}))

(defn handle-load [request]
  (println "LOAD:")
  (let [
        name (-> request :params :name)
        board (loadBoard name)
        result {:board board :status GAME_IN_PROGRESS}
        ]
    (println name)
    (println result)
    ; save to file:
    ;(spit (str "/tmp/" name ".json") board)
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (str (json/write-str result))})
  )

(defn handle-load-json [request]
  (wrap-json-body handle-load {:keywords? true}))

(defn validateProgramArguments [args]
    (def defaultProgramArguments ["human" "cpuOpponentRandomMoves"])
    (if-not (= (count args) 2)
        defaultProgramArguments
        (if (not (set/subset? (into #{} args) validPlayers))
            defaultProgramArguments
            args
        )
    )
)

; ------------------- App --------------------------------
(defroutes app-routes
           ;(GET "/tictactoe" [] (resp/content-type (resp/resource-response "client.html") "text/html"))
           ;(GET "/tictactoe" [] (resp/content-type (resp/resource-response "client.html") "text/html"))
           (GET "/tictactoe" [] (resp/redirect "client.html"))
           (POST "/tictactoe/move" [] handle-new-move-json)
           (POST "/tictactoe/game/:name" [] handle-save-json)
           (GET "/tictactoe/game/:name" [] handle-load-json)
           (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  ; args =  ["<first player>" "<second player>"]
  ; Can be:
  ;  ["human" "human"] - no CPU player, only the game board will be provided by the application
  ;  ["human" "cpuOpponentRandomMoves"] - Human player starts, the opponent is "cpuOpponentRandomMoves" (default)
  ;  ["human" "cpuOpponentBetterMoves"] - Human player starts, the opponent is a different CPU player.
  ;  ["cpuOpponentRandomMoves" "human"] - CPU player starts.
  ;  ["cpuOpponentRandomMoves" "cpuOpponentBetterMoves"] - Two CPU players, no user interaction.
  ;
  ; valid players are provided in the set validPlayers
  [& args]

  ;(def validProgramArguments (validateProgramArguments args))

  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))
        routeHandler (wrap-defaults #'app-routes api-defaults)
        ]
    ; Run the server with Ring.defaults middleware
    (server/run-server (-> routeHandler
                           (wrap-resource "public")) {:port port})
    ; Run the server without ring defaults
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
