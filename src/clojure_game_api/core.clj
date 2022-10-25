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
  (< (count (keys board)) 9))

(defn validateBalance [board]
  (if (= board initialBoard)
    true
    (let [freq            (frequencies (vals board))
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

; CPU player 1 - makes a random move and returns the board
(defn cpuOpponentRandomMoves [board value]
  (def targetField (get (into [] validCoordinates) (rand-int 9)))
  (if-not (= nil (get board targetField))
    (cpuOpponentRandomMoves board value)
    (makeMove board targetField value)))


(defn tictactoe-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @tictactoe-board))})

(use
  '[ring.middleware.json :only [wrap-json-body]]
  '[ring.util.response :only [response]])

(defn stringMapToKeywordMap [inMap]
  (into {} (for [[k v] inMap] [k (keyword v)])))

(defn handle-new-move [request]
  (let [board    (stringMapToKeywordMap (get-in request [:body :board]))
        newBoard (makeMove board
                           (keyword (get-in request [:body :move :field])),
                           (keyword (get-in request [:body :move :value])))]

  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str newBoard))})
  )


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
