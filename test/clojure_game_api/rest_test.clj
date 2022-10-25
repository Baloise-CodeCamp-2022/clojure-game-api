; see https://github.com/ring-clojure/ring-mock
(ns clojure-game-api.rest-test
  (:require [clojure-game-api.core :refer :all]
            [clojure.test :refer :all]
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
    (is
      (=
        (handle-new-move-json
          (->
            (mock/request :post "/tictactoe/move")
            (mock/json-body {:board {:B1 "X" :B2 "O"} :move {:field "A1" :value "X"}})))
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    "{\"B1\":\"X\",\"B2\":\"O\",\"A1\":\"X\"}"}
        )))
  )
 