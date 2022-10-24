(ns clojure-game-api.core-test
  (:require [clojure.test :refer :all]
            [clojure-game-api.core :refer :all]))

(deftest a-test
  (testing "first simple test"
    (is (= 1 1))))

(deftest first
  (def board #{ {:B2 true} {:A1 true} })
  (def expected #{{:A1 true} {:B2 true} {:A2 true}})
  (testing "first board test"
    (is (= expected (makeMove board :A2, true)))))
