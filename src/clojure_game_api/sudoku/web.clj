(ns clojure-game-api.sudoku.web
  (:require [clojure-game-api.main.maincore :refer :all]
            [clojure-game-api.sudoku.core :refer :all]
            [clojure.java.io :refer :all]
            [compojure.core :refer :all]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.file :refer :all]
            [ring.middleware.resource :refer :all])
  (:gen-class))


(defn handle-sudoku-load-json [request]
  (let [
        load-fn (di-get :load)
        name (-> request :params :name)
        board (apply load-fn name '())
        result {:board board}
        ]
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (str (json/write-str result))})
  )

(def sudoku-handlers '(
  (GET "/sudoku/:id" [id] (str "<h1>Hello user " id "</h1>"))
  (GET "/sudoku/game/:name" [] handle-sudoku-load-json)
  ))

; ------------------- App --------------------------------
;(defroutes sudoku-routes)

