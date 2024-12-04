(ns aoc.2024.day3
  (:require [clojure.string :as str]))

(def example "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")


(defn part1-solve [input]
  (reduce + 0 (map #(apply * %)
                   (map (fn [pair]
                          (map #(Integer/parseInt %) pair))
                        (map #(str/split % #" ")
                             (map #(str/replace % #"\(|\)|\[|\]|\"" "")
                                  (map #(str/split % #",") 
                                       (map second (re-seq #"mul(\(\d+\,\d+\))" input)))))))))

(def puzzle-input (slurp "resources/2024/day3.puzzle"))

(part1-solve puzzle-input)

(def p2-example "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")


(re-seq #"mul(\(\d\,\d\))|don\'t\(\)|do\(\)" p2-example)

(defn parse-mul [mul-str]
  "Parses a mul string like '(2,4)' and returns the product of the two numbers."
  (when (re-matches #"\(\d+,\d+\)" mul-str)
    (let [[a b] (map #(Integer/parseInt %)
                     (clojure.string/split (subs mul-str 1 (dec (count mul-str))) #","))]
      (* a b))))


(defn part2-solve [data]
  "Processes the input data, stopping multiplication on 'don't()' until 'do()' is seen."
  (loop [remaining data
         allow-multiply true
         results []]
    (if (empty? remaining)
      results
      (let [[[instr args] & rest-data] remaining
            next-state
            (cond
              (= instr "don't()") [false results]
              (= instr "do()") [true results]
              (and allow-multiply (some? args)) [allow-multiply (conj results (parse-mul args))]
              :else [allow-multiply results])]
        (recur rest-data (first next-state) (second next-state))))))

(reduce + 0 (process-data (re-seq #"mul(\(\d+\,\d+\))|don\'t\(\)|do\(\)" puzzle-input)))
