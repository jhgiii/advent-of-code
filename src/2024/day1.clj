(ns aoc.2024.day1
 (:require [clojure.string :as str]))

(def sample-input (slurp "resources/2024/day1.sample"))
(def puzzle-input (slurp "resources/2024/day1.puzzle"))

;;Part 1
(defn split-lists [input]
  (let [rows (map #(map read-string (str/split % #"\s+"))
                  (str/split-lines input))]
    (apply map vector rows)))

(defn subtract-lists [list-of-lists]
  (let [list1 (first list-of-lists)
        list2 (second list-of-lists)]
    (map (fn [x y] (abs (- x y))) list1 list2))) 

(defn solve-part1 [puzzle-input]
  (reduce + (->> puzzle-input
                 split-lists
                 (map sort)
                 subtract-lists)))

(solve-part1 puzzle-input)

;;Part 2
(defn count-occurrences [input]
  (let [list1 (first input)
        list2 (second input)
        freq-map (frequencies list2)]
    (reduce (fn [result-map elem]
              (assoc result-map elem (get freq-map elem 0)))
            {}
            list1)))

(def look-up-table (count-occurrences (split-lists puzzle-input)))

(def result
  (reduce
   (fn [acc element]
     (if (contains? look-up-table element)
       (conj acc (* element (look-up-table element)))
       acc))
   []
   (first (split-lists puzzle-input))))

(reduce + result)

