(ns aoc.2015.day1
  (:require [clojure.string :as str]))


;;Day 1 Part 1 - Count "(" and ")" where "(" increments and ")" decrements
;; (()) and ()() both equal 0
;; Start at 0

(def example ")())())")
(def puzzle (slurp "resources/2015/day1/puzzle-input"))

(defn solve1 [input]
  (->> input
       ;;trim new line if necessary
       str/trim-newline
       ;;convert each '(' to 1 and each ')' to -1
       (map (fn [x] (if (= \( x ) 1 -1)))
       ;;Sum the vector
       (reduce +)))

(solve1 example)
(solve1 puzzle)


;;Need to determine at which point we first enter the basement, meaning the cumulative count < 0

(defn solve2 [input]
  ;;Use of reductions will produce the running sum
  (->>  (reductions + 0
                    ;;map the array set to the input, producing a vector of 1s and -1s
                    (map {\( 1 \) -1} input))
        ;; We will take values until we reach the basement (when total becomes -1)
       (take-while #(not= % -1))
       ;; count the number elements in the vector returned from take-while
       count))
  
(solve2 puzzle)

