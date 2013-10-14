(ns ninety-nine-problems.lists
    (:require
        [clojure.contrib.core :refer :all])
    (:gen-class))

(defn last'
    [coll]
    (let
        [[head & tail] coll]
        (if (empty? tail)
            head
            (recur tail))))

(defn but-last
    [coll]
    (let
        [[head snd & tail] coll]
        (if (nil? snd)
            nil
        (if (empty? tail)
            head
            (recur (conj tail snd))))))

(defn element-at
    [coll k]
    (if (empty? coll)
        nil
    (if (= k 1)
        (first coll)
        (recur (rest coll) (dec k)))))

(def length
    (comp
        (partial reduce +)
        (partial map (constantly 1))))

(defn reverse'
    [coll]
    (if (empty? coll)
        '()
        (reduce conj '() coll)))

(defn palindrome?
    [coll]
    (= coll (reverse' coll)))

(defn flatten'
    [coll]
    (if (empty? coll)
        '()
        (let [[head & tail] coll]
            (if (seq? head)
                (concat (flatten' head) (flatten' tail))
                (conj (flatten' tail) head)))))

(def compress
    (comp
        (partial map first)
        (partial partition-by identity)))

(def pack (partial partition-by identity))

(def encode 
    (comp
        (partial map #(vector (count %) (first %)))
        pack))

(def encode'
    (comp
        (partial map
            (fn [coll]
                (let [[head & tail] coll]
                    (if (empty? tail)
                        head
                        [(count coll) head]))))
        pack))

(def decode
    (partial mapcat
        (fn [encoded]
            (if (vector? encoded)
                (apply replicate encoded)
                `(~encoded)))))

(def duplicate
    (partial mapcat (partial replicate 2)))

(defn replicate'
    [coll n]
    (mapcat (partial replicate n) coll))

(defn drop-every
    [coll n]
    (mapcat 
        (fn [part]
            (if (= (count part) n)
                (butlast part)
                part))
        (partition-all n coll)))

(defn split
    [coll n]
    (if (empty? coll)
        ['() '()]
    (if (zero? n)
        ['() coll]
        (let 
            [[head & tail]  coll
            [before after]  (split tail (dec n))]
            [(conj before head) after]))))

(defn slice
    [coll m n]
    (->> coll
        (drop (dec m))
        (take (inc (- n m)))))

(defn rotate
    [coll n]
    (let [length (count coll)]
        (if (neg? n)
            (recur coll (+ length n))
        (if (> n length)
            (recur coll (mod n length))
            (let [[before after] (split coll n)]
                (concat after before))))))

(defn remove-at
    [coll n]
    (let [[before [_ & after]] (split coll (dec n))]
        (concat before after)))

(defn insert-at
    [x coll n]
    (let [[before after] (split coll (dec n))]
        (if (empty? after)
            (if (= (count before) (dec n))
                (concat before (replicate 1 x))
                before)
            (concat before (conj after x)))))

(defn range'
    [m n]
    (take (inc (- n m)) (iterate inc m)))

(defn combinations
    [k coll]
    (if (zero? k)
        '(())
    (if (empty? coll)
        nil
        (let
            [[head & tail] coll
            with-head (combinations (dec k) tail)
            without-head (combinations k tail)]
            (concat (map #(conj % head) with-head) without-head)))))

(defn elem?
    [x coll]
    (some #{x} coll))

(defn diff
    [coll coll']
    (filter #(not (elem? % coll')) coll))

(defn group
    [coll groups]
    (if (empty? groups)
        '(())
        (let
            [[n & ns] groups
            heads (combinations n coll)]
            (mapcat
                (fn [head]
                    (map #(conj % head) (group (diff coll head) ns)))
                heads))))

(def sort-by-length
    (partial sort-by count))

(defn sort-by-length-frequency
    [coll]
    (let [freqs (frequencies (map count coll))]
        (sort-by #(freqs (count %)) coll)))
