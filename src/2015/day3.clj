(ns day3)
(def example1 \>)
(def example2 "^>v<")
(def example3 "v^v^v^v^")

;;define a map translation of directional strings to directional vectors
(def moves
  {">" [1 0]
   "^" [0 1]
   "<" [-1 0]
   "v" [0 -1]})

;;convert the string input into directional vectors
(defn parse-movements
  "Given a string value of the directions a directional vector is returned"
  [input]
  (replace moves (map str (seq input))))

(defn next-house
  "x: the current location in form [[x y] #{x' y'}] where the first vec is last location and the set is the set of visited locations
   y: the next directional vector.
  Return: [[y] #{y x ...}]"
  [x y]
  (let [nxt (map + (first x) y)]
    [nxt (conj (last x) nxt)]))

;;Starting position to act as an accumulator for the "next-house" and "track-houses" functions
;;the first element is the last visited location on a 2d grid
;;the second element, the set, is the set of locations already visited. This will be and accumulator.
(def start [[0 0] #{[0 0]}])

(defn track-houses
  "Given a seq of moves as vectors, returns a set of all visited houses."
  [moves]
  (last (reduce next-house start moves)))

;;read in puzzle input
(def puzzle (slurp "resources/day3.puzzle.input"))

;; Part 1 Solution
(->> puzzle
     parse-movements
     track-houses
     count)

((comp count set) (apply concat (map track-houses [(take-nth 2 (parse-movements puzzle))
                                                   (take-nth 2 (rest (parse-movements puzzle)))])))
