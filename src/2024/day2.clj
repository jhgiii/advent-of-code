(ns aoc.2024.day2
  (:require [clojure.string :as str]))

(def puzzle-input 
  (->> (slurp "resources/2024/day2.puzzle")
       str/split-lines
       (map #(str/split % #"\s+"))
       (map (fn [row] (map #(Integer/parseInt %) row)))))

;;Part 1
(defn valid-distances? [coll]
  (let [distances (map #(abs (- %1 %2)) coll (rest coll))]
    (every? #(<= 1 % 3) distances)))

(defn strictly-increasing? [coll]
  "Checks if the list is strictly increasing"
  (every? true? (map < coll (rest coll))))

(defn strictly-decreasing? 
  [coll]
  "Checks if a collection is strictly decreasing"
  (every? true? (map > coll (rest coll))))

(defn safe?
  [coll]
  "Pred function to test safety of report for AOC2024 Day 2.
  Takes list of integers, returns bool.
  Checks if seq is either increasing OR decreasing 
  AND checks that distance between any adjacent elements is at most 3 AND at least 1"
  (and (or (strictly-increasing? coll)
           (strictly-decreasing? coll))
       (valid-distances? coll)))

(count (filter safe? puzzle-input))

;;Part 2

(defn part2-safe? [coll]
  (or (safe? coll) ;; Already safe
      (some (fn [i]
              (safe? (concat (take i coll) (drop (inc i) coll))))
            (range (count coll)))))


(->> puzzle-input
     (map part2-safe?)
     (remove nil?)
     count)






