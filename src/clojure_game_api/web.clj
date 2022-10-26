(ns clojure-game-api.web
  (:require [clojure-game-api.core :refer :all]
            [clojure.data.json :as json]
            [clojure.java.io :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all]
            [ring.util.response :as resp])
  (:gen-class))


(defn handle-new-move [request]
  (let [board (stringMapToKeywordMap (get-in request [:body :board]))
        afterCallerMove (makeMove board
                                  (keyword (get-in request [:body :move :field])),
                                  (keyword (get-in request [:body :move :value])))
        returnBoardAndStatus (if (or (= GAME_WON (afterCallerMove :status)) (= GAME_LOST (afterCallerMove :status)))
                               afterCallerMove
                               (cpuOpponent2 (afterCallerMove :board) :O))
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
        ]2
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

; ------------------- App --------------------------------
(defroutes app-routes
           (GET "/tictactoe" [] (resp/redirect "client.html"))
           (POST "/tictactoe/move" [] handle-new-move-json)
           (POST "/tictactoe/game/:name" [] handle-save-json)
           (GET "/tictactoe/game/:name" [] handle-load-json)
           (route/not-found "Error, page not found!"))
