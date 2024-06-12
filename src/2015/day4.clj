(ns day4
  (:require
   [digest]))

;;Memoization for hashing
(def memoize-md5 (memoize digest/md5))

(defn pos-ints
  "Create lazy sequence of positive integers"
  ([] pos-ints 1)
  ([x] (lazy-seq (cons x (pos-ints (inc x))))))

(defn generate-md5
  "Generates an MD5 for the given Prefix and a positive integer suffix
  ie: prefix: abcde
      suffix: 1"
  [prefix suffix]
  (memoize-md5 (str prefix suffix)))

;;Testing function
(assert (= (generate-md5 "abcdef" "609043") "000001dbbfa3a5c83a2d506429c7b00e"))

(defn list-of-hashes
  "Returns list the MD5 Hash of the concatenated prefix and suffix along with the suffix for tracking purposes"
  [prefix suffix]
  (list (generate-md5 prefix suffix) suffix))

;; Testing list-of-hashes
(assert (= (list-of-hashes "abcdef" "609043") ["000001dbbfa3a5c83a2d506429c7b00e" "609043"]))

(defn solve
  "Solves AOC2015 Day 4. Zeros is the number of leading zeros to search for"
  [input zeros]
  (->> (pos-ints 1)
       (map #(list-of-hashes input %))
       (filter #(-> % first (.startsWith zeros)))
       first
       second))

