(ns clojure-game-api.core-test
  (:require [clojure.test :refer :all]
            [clojure-game-api.core :refer :all]))

(deftest a-test
  (testing "first simple test"
    (is (= 1 1))))

(deftest first
  (testing "first board test"
    (is (= [{:A1 true} {:B2 true} {"A2" true}] (makeMove "A2", true)))))
