(ns day6
  (:require
   [clojure.string :as str]))

(def puzzle-input (slurp "resources/day6.puzzle.input"))

(def grid-size 1000)

(defn create-grid []
  (int-array (* grid-size grid-size)))

(defn coords->index [x y]
  (+ (* x grid-size) y))

(defn parse-instruction [line]
  (let [[_ action x1 y1 x2 y2]
        (re-matches #"(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)" line)]
    {:action action
     :x1 (Integer/parseInt x1)
     :y1 (Integer/parseInt y1)
     :x2 (Integer/parseInt x2)
     :y2 (Integer/parseInt y2)}))

(defn apply-instruction [grid {:keys [action x1 y1 x2 y2]}]
  (let [width grid-size]
    (doseq [x (range x1 (inc x2))]
      (let [start (coords->index x y1)
            end   (coords->index x y2)]
        (dotimes [offset (inc (- y2 y1))]
          (let [idx (+ start offset)]
            (aset-int grid idx
                      (case action
                        "turn on"  1
                        "turn off" 0
                        "toggle"   (if (= (aget grid idx) 0) 1 0)))))))))

(defn count-lights-on [grid]
  (reduce + (seq grid)))

(defn solve-part-1 [input]
  (let [grid (create-grid)
        instructions (map parse-instruction (clojure.string/split-lines input))]
    (doseq [instruction instructions]
      (apply-instruction grid instruction))
    (count-lights-on grid)))

(solve-part-1 puzzle-input)

(defn apply-instruction [grid {:keys [action x1 y1 x2 y2]}]
  (doseq [x (range x1 (inc x2))]
    (let [row-start (+ (* x grid-size) y1)
          row-end (+ (* x grid-size) y2)]
      (dotimes [offset (inc (- y2 y1))]
        (let [idx (+ row-start offset)
              current (aget grid idx)]
          (aset-int grid idx
                    (case action
                      "turn on"  (inc current)
                      "turn off" (max 0 (dec current))
                      "toggle"   (+ current 2))))))))

(defn total-brightness [grid]
  (reduce + (seq grid)))

(defn solve-part-2 [input]
  (let [grid (create-grid)
        instructions (map parse-instruction (str/split-lines input))]
    (doseq [instruction instructions]
      (apply-instruction grid instruction))
    (total-brightness grid)))
(solve-part-2 puzzle-input)
