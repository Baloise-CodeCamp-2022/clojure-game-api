(ns clojure-game-api.main.maincore
  (:gen-class))

; ------------------- dependency injection --------------------------------
(def di-context (atom {}))
(defn di-put [key value]
  (swap! di-context assoc (keyword key) value))

(defn di-get [key]
  (get @di-context (keyword key) ))

(defn di-reset [m]
  (reset! di-context m))
