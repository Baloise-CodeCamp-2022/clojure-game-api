; see https://github.com/ring-clojure/ring-mock
(ns clojure-game-api.rest-test
  (:require [clojure.test :refer :all]
            [clojure-game-api.core :refer :all]
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

