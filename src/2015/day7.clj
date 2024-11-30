(ns day7
  (:require
   [clojure.string :as str]))

(def sample-input (slurp "resources/day7.sample.input"))

(def patterns
  {#"(\d+) -> (\w+)"
   (fn [[_ value target]]
     {:op :assign, :value (Integer/parseInt value), :target (keyword target)})

   #"(\w+) -> (\w+)"
   (fn [[_ source target]]
     {:op :assignvar, :source (keyword source), :target (keyword target)})

   #"(\d+|\w+) AND (\d+|\w+) -> (\w+)"
   (fn [[_ left right target]]
     {:op :and
      :left (if (re-matches #"\d+" left) (Integer/parseInt left) (keyword left))
      :right (if (re-matches #"\d+" right) (Integer/parseInt right) (keyword right))
      :target (keyword target)})

   #"(\d+|\w+) OR (\d+|\w+) -> (\w+)"
   (fn [[_ left right target]]
     {:op :or
      :left (if (re-matches #"\d+" left) (Integer/parseInt left) (keyword left))
      :right (if (re-matches #"\d+" right) (Integer/parseInt right) (keyword right))
      :target (keyword target)})

   #"(\w+) LSHIFT (\d+) -> (\w+)"
   (fn [[_ left amount target]]
     {:op :lshift
      :left (keyword left)
      :amount (Integer/parseInt amount)
      :target (keyword target)})

   #"(\w+) RSHIFT (\d+) -> (\w+)"
   (fn [[_ left amount target]]
     {:op :rshift
      :left (keyword left)
      :amount (Integer/parseInt amount)
      :target (keyword target)})

   #"NOT (\w+) -> (\w+)"
   (fn [[_ operand target]]
     {:op :not, :operand (keyword operand), :target (keyword target)})})

(defn parse-instruction [line]
  (some (fn [[pattern handler]]
          (when-let [matches (re-matches pattern line)]
            (handler matches)))
        patterns))

(defn evaluate [wire state instructions]
  (if (contains? state wire)
    ;; Return the cached value and state
    (do
      (println "Cached state:" state)
      [(state wire) state])
    ;; Otherwise, compute the value
    (let [instr (first (filter #(= (:target %) wire) instructions))]
      (if (nil? instr)
        (throw (IllegalArgumentException. (str "No instruction found for wire: " wire)))
        (let [[value updated-state]
              (case (:op instr)
                ;; Handle numeric assignment
                :assign
                (let [value (bit-and (:value instr) 0xFFFF)] ; Mask to 16 bits
                  [value (assoc state wire value)])

                ;; Handle wire-to-wire assignment
                :assignvar
                (let [[source-value updated-state] (evaluate (:source instr) state instructions)]
                  [source-value (assoc updated-state wire source-value)])

                ;; Handle AND operation
                :and
                (let [[left-value updated-state1]
                      (if (keyword? (:left instr))
                        (evaluate (:left instr) state instructions)
                        [(:left instr) state])
                      [right-value updated-state2]
                      (if (keyword? (:right instr))
                        (evaluate (:right instr) updated-state1 instructions)
                        [(:right instr) updated-state1])
                      value (bit-and (bit-and left-value right-value) 0xFFFF)]
                  [value (assoc updated-state2 wire value)])

                ;; Handle OR operation
                :or
                (let [[left-value updated-state1]
                      (if (keyword? (:left instr))
                        (evaluate (:left instr) state instructions)
                        [(:left instr) state])
                      [right-value updated-state2]
                      (if (keyword? (:right instr))
                        (evaluate (:right instr) updated-state1 instructions)
                        [(:right instr) updated-state1])
                      value (bit-and (bit-or left-value right-value) 0xFFFF)]
                  [value (assoc updated-state2 wire value)])

                ;; Handle NOT operation
                :not
                (let [[operand-value updated-state] (evaluate (:operand instr) state instructions)
                      value (bit-and (bit-not operand-value) 0xFFFF)]
                  [value (assoc updated-state wire value)])

                ;; Handle LSHIFT operation
                :lshift
                (let [[left-value updated-state] (evaluate (:left instr) state instructions)
                      value (bit-and (bit-shift-left left-value (:amount instr)) 0xFFFF)]
                  [value (assoc updated-state wire value)])

                ;; Handle RSHIFT operation
                :rshift
                (let [[left-value updated-state] (evaluate (:left instr) state instructions)
                      value (bit-and (bit-shift-right left-value (:amount instr)) 0xFFFF)]
                  [value (assoc updated-state wire value)]))]
          (println "Wire:" wire "State after update:" updated-state)
          [value updated-state])))))


(def puzzle-input (slurp "resources/day7.puzzle.input"))
(def puzzle-instructions (map parse-instruction (str/split-lines puzzle-input)))
puzzle-instructions
(evaluate :a {} puzzle-instructions)
