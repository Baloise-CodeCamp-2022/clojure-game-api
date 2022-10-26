(ns clojure-game-api.tictactoe.web
  (:require [clojure-game-api.tictactoe.core :refer :all]
            [clojure-game-api.main.maincore :refer :all]
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
  (if (= @GAME_STATE [GAME_IN_PROGRESS])
    (let [board (stringMapToKeywordMap (get-in request [:body :board]))
          afterCallerMove (makeMove board
                                    (keyword (get-in request [:body :move :field])),
                                    (keyword (get-in request [:body :move :value])))
          returnBoardAndStatus (if (or (= GAME_WON (afterCallerMove :status)) (= GAME_LOST (afterCallerMove :status)))
                                  afterCallerMove
                                 (cpuOpponentRandomMoves (afterCallerMove :board) :O))
          ]
      {:status  200
       :headers {"Content-Type" "text/json"}
       :body    (str (json/write-str returnBoardAndStatus))}
    )))

(use
  '[ring.middleware.json :only [wrap-json-body]]
  '[ring.util.response :only [response]])
(def handle-new-move-json (wrap-json-body handle-new-move {:keywords? true}))

; ----------------------------------
(defn handle-save [request]
  (println "SAVE:")
  (println (di-get :save))
  (let [
        save-fn (di-get :save)
        name (-> request :params :name)
        board (stringMapToKeywordMap (get-in request [:body :board]))
        result board
        ]
    (println name)
    (apply save-fn name board '())
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (str (json/write-str result))})
  )

(defn handle-save-json [request]
  (wrap-json-body handle-save {:keywords? true}))

(defn handle-load [request]
  (println "LOAD:")
  (let [
        load-fn (di-get :load)
        name (-> request :params :name)
        board (apply load-fn name '())
        result {:board board :status GAME_IN_PROGRESS}
        ]
    (println name)
    (println result)
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (str (json/write-str result))})
  )

(defn handle-load-json [request]
  (wrap-json-body handle-load {:keywords? true}))

(defn handle-get-tictactoe [request]
      (reset! GAME_STATE [GAME_IN_PROGRESS])
      (resp/content-type (resp/resource-response "client.html" {:root "public"}) "text/html"))

; ------------------- App --------------------------------
(defroutes app-routes
           (GET "/tictactoe" [] handle-get-tictactoe)
           (POST "/tictactoe/move" [] handle-new-move-json)
           (POST "/tictactoe/game/:name" [] handle-save-json)
           (GET "/tictactoe/game/:name" [] handle-load-json)
           (route/not-found "Error, page not found!"))

