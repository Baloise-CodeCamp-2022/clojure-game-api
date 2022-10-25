(ns clojure-game-api.core
  (:require [clojure.data.json :as json]
            [clojure.set :as set]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [ring.middleware.defaults :refer :all])
  (:gen-class))

; ------------------- TicTacToe --------------------------------
(def tictactoe-board (atom []))

(def validCoordinates #{:A1, :A2, :A3, :B1, :B2, :B3, :C1, :C2, :C3})
(def validValues #{:X, :O})

(def initialBoard {})

(defn validateBoardNotFull [board]
  (< (count (keys board)) 9)
)

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
       (validateBalance board)
       (validateBoardNotFull board)))

(defn makeMove [board coordinate value]
  (if (not (contains? validCoordinates coordinate))
    (throw (Exception. (str "invalid field " coordinate))))
  (if (not (contains? validValues value))
    (throw (Exception. (str "invalid value " value))))
  (if (contains? board coordinate)
    (throw (Exception. (str "field already set" coordinate))))
  (if (not (validateBoard board))
    (throw (Exception. (str "board is invalid" board))))

  (conj board {coordinate value}))


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


; CPU player 1 - makes a random move and returns the board
(defn cpuOpponentRandomMoves [board value]
     (def validMoves (set/difference validCoordinates (into #{} (keys board))))
     (def targetField (get  (into [] validMoves) (rand-int (count validMoves))))
     (makeMove board targetField value)
)


(defn tictactoe-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @tictactoe-board))})

(use '[ring.middleware.json :only [wrap-json-body]]
     '[ring.util.response :only [response]])

(defn handle-new-move [request]
  (prn request)
  (prn (get-in request [:body :board]))
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @tictactoe-board))})



; ------------------- App --------------------------------
(defroutes app-routes
           (GET "/tictactoe" [] tictactoe-handler)
           (POST "/tictactoe/move" [] (wrap-json-body handle-new-move {:keywords? true}))
           (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes api-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
