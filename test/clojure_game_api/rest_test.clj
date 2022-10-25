; see https://github.com/ring-clojure/ring-mock
(ns clojure-game-api.rest-test
  (:require [clojure-game-api.core :refer :all]
            [clojure.test :refer :all]
            [clojure.string :as string]
            [ring.mock.request :as mock]))

(deftest a-test
  (testing "first simple test"
    (is (= 1 1))))

(deftest rest-test
  (testing "GET request to /tictactoe returns expected response"
    (is (= (tictactoe-handler (mock/request :get "/tictactoe"))
           {:status  200
            :headers {"Content-Type" "text/json"}
            :body    "[]"}))))

(deftest new-move-test
  (testing "POST request to 'tictactoe/move' returns expected response"
    (let [response (handle-new-move-json
                     (->
                       (mock/request :post "/tictactoe/move")
                       (mock/json-body {:board {:B1 "X" :B2 "O"} :move {:field "A1" :value "X"}})))
          ]
      (is (= (:status response) 200))
      (is (= (get-in response [:headers "Content-Type"]) "text/json"))
      (is (string/starts-with? (get-in response [:body]) "{\"B1\":\"X\",\"B2\":\"O\",\"A1\":\"X\""))
      )
    ))
