; see https://github.com/ring-clojure/ring-mock
(ns clojure-game-api.tictactoe.rest-test
  (:require [clojure-game-api.tictactoe.core :refer :all]
            [clojure.string :as string]
            [clojure.test :refer :all]
            [clojure-game-api.tictactoe.web :refer :all]
            [ring.mock.request :as mock]))

(deftest new-move-test
  (testing "POST request to 'tictactoe/move' returns expected response"
    (let [response (handle-new-move-json
                     (->
                       (mock/request :post "/tictactoe/move")
                       (mock/json-body {:board {:B1 "X" :B2 "O"} :move {:field "A1" :value "X"}})))
          ]
      (is (= (:status response) 200))
      (is (= (get-in response [:headers "Content-Type"]) "text/json"))
      (is (string/starts-with? (get-in response [:body]) "{\"board\":{\"B1\":\"X\",\"B2\":\"O\",\"A1\":\"X\""))
      )
    ))
