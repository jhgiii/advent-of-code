(ns day5
  (:require
   [clojure.string :as str]))

;;Read in the data
(def puzzle (slurp "resources/day5.puzzle.input"))

;;example input
(def example ["ugknbfddgicrmopn" "aaa" "jchzalrnumimnmhp" "haegwjzuvuyypxyu" "dvszwmarrgswjxmb"])

;;solve part1
(->> puzzle
     str/split-lines
     (remove #(re-find #"ab|cd|pq|xy" %))
     (filter #(re-find #"(.*[aeiou]){3}" %))
     (filter #(re-find #"(.)\1" %))
     count)

;;solve part 2
(->> puzzle
     str/split-lines
     (filter #(re-find #"(..).*\1" %))
     (filter #(re-find #"(.).\1" %))
     count)
