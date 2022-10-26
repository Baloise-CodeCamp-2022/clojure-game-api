(ns clojure-game-api.main.main
  (:require
    [clojure-game-api.main.maincore :refer :all]
    [clojure-game-api.main.mainweb :refer :all]
    [clojure-game-api.tictactoe.main :refer :all]
    [clojure-game-api.tictactoe.web :refer :all]
    )
  (:gen-class))

(defn -main
  [& args]

  (di-reset tictactoe-di-context)
  ;(tictactoe args)
  (start-web-server #'app-routes)
  )
