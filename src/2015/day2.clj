(ns day2
  (:require
   [clojure.string :as str]))

;; Given dimensions of a box in Length, Width, and Height in the form LxWxH, find the surface area
;; of each box with the addition of the smallest dimension added as extra

;;Function:  f(l,w,h) = (2lw + 2wh + 2hl) + min(l,w,h)

(def puzzle (slurp "resources/day2.puzzle.input"))

(defn surface-area
  [dimensions]
  (let [[l w h] (sort dimensions)]
    (+ (* 3 l w)
       (* 2 w h)
       (* 2 h l))))

(defn ribbon-length
  [dimensions]
  (let [[x y z] (sort dimensions)]
    (+ (+ (+ x x)
          (+ y y))
       (* x y z))))

(defn dimensions
  [string]
  (map #(Integer. %) (str/split string #"x")))

(->> puzzle
     str/split-lines
     (map dimensions)
     (map surface-area)
     (reduce +))

(->> puzzle
     str/split-lines
     (map dimensions)
     (map ribbon-length)
     (reduce +))

