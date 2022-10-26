(ns clojure-game-api.main.main
  (:require
    [clojure-game-api.main.maincore :refer :all]
    [clojure-game-api.main.mainweb :refer :all]
    [clojure-game-api.tictactoe.main :refer :all]
    [clojure-game-api.tictactoe.web :refer :all]
    [clojure-game-api.sudoku.web :refer :all]
    [compojure.core :refer :all]
    [compojure.route :as route]
    )
  (:gen-class))

(def app-routes
  (routes
    (GET "/tictactoe" [] handle-get-tictactoe)
    (POST "/tictactoe/move" [] handle-new-move-json)
    (POST "/tictactoe/game/:name" [] handle-save-json)
    (GET "/tictactoe/game/:name" [] handle-load-json)
    (GET "/sudoku/:id" [id] (str "<h1>Hello user " id "</h1>"))
    (route/not-found "Error, page not found!"))
  )


;(def app-routes
;  (apply routes (concat ttt-handlers sudoku-handlers)))

(defn -main
  [& args]

  (di-reset tictactoe-di-context)
  ;(tictactoe args)
  ;(start-web-server #'app-routes)
  (start-web-server #'app-routes)
  )
