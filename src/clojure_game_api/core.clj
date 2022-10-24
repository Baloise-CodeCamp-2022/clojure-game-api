(ns clojure-game-api.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.data.json :as json])
  (:gen-class))

; ------------------- TicTacToe --------------------------------
(def tictactoe-board (atom []))

(def validCoordinates #{:A1, :A2, :A3, :B1, :B2, :B3, :C1, :C2, :C3})
(def validValues #{:X, :O})

(def initalBoard {})

(defn validateBalance [board]
  (if (= board initalBoard)
    true
    (let [freq            (frequencies (vals board))
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
  (if (not (validateBoard board))
    (throw (Exception. (str "board is invalid" board))))

  (conj board {coordinate value}))

(makeMove nil :A1 :X)
(makeMove nil :B2 :O)

(defn tictactoe-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @tictactoe-board))})

; ------------------- App --------------------------------
(defroutes app-routes
  (GET "/tictactoe" [] tictactoe-handler)
  (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
