(ns cards-clojure.core
  (use [clojure.set])
  (:gen-class))

(def suits [:clubs :spades :hearts :diamonds])
(def ranks (range 1 14))
(def rank-names {1 :ace, 11 :jack, 12 :queen, 13 :king})
(def rank-numbers {:ace 1, :jack 11, :queen 12, :king 13})

(defn create-deck []
  (set
    (for [suit suits
          rank ranks]
      {:suit suit
       :rank (get rank-names rank rank)})))

(defn name-values [vals-sorted]    ;need to convert back to numbers for math. 
  (for [item vals-sorted]
    (get rank-numbers item item)))

(defn create-hands [deck]
  (set
    (for [c1 deck
          c2 (disj deck c1)
          c3 (disj deck c1 c2)
          c4 (disj deck c1 c2 c3)]
      #{c1 c2 c3 c4})))

(defn flush? [hand]
  (= 1 (count (set (map :suit hand)))))
        
(defn strait? [hand]     ;the number i get from this follows this math 4^4*10  https://en.wikipedia.org/wiki/List_of_poker_hands#Straight
  (let [vals (set (map :rank hand))
        vals (set (name-values vals))
        vals-sorted (sort vals)]
    (and
      (= 4 (count vals-sorted))
      (= 3 (- (last vals-sorted) (first vals-sorted))))))

(defn four-of-a-kind? [hand]
  (= 1 (count (set (map :rank hand)))))
;
;(defn three-of-a-kind? [hand]
;   (let [vals (map :rank hand)
;         vals (name-values vals)]
;         ;vals-sorted (sort vals)] 
;      (= 1 (count 
;              (difference #{(nth vals 0)} #{(nth vals 1)} #{(nth vals 2)} #{(nth vals 3)})))))
 
;dont think this is quite correct
(defn three-of-a-kind? [hand]
  (let [vals (map :rank hand)
        vals (name-values vals)
        vals-sorted (sort vals)
        first-val (nth vals-sorted 0)
        second-val (nth vals-sorted 1)
        third-val (nth vals-sorted 2)
        fourth-val (nth vals-sorted 3)]
    (or (= first-val (/ (+ first-val second-val third-val) 3))
        (= fourth-val (/ (+ second-val third-val fourth-val) 3)))))

(defn two-pair? [hand]
  (let [vals (map :rank hand)
        vals (name-values vals)
        vals-sorted (sort vals)
        first-val (nth vals-sorted 0)
        second-val (nth vals-sorted 1)
        third-val (nth vals-sorted 2)
        fourth-val (nth vals-sorted 3)]
    (and (= first-val second-val)
        (= third-val fourth-val))))
    
     
  
(defn -main []
  (let [deck (create-deck)
        hands (create-hands deck)
        flush-hands (count (filter flush? hands))
        strait-hands (count (filter strait? hands))
        strait-flush-hands (count (filter strait? flush-hands))
        four-of-a-kind-hands (count (filter four-of-a-kind? hands))
        three-of-a-kind-hands (count (filter three-of-a-kind? hands))
        two-pair-hands (count (filter two-pair? hands))]
    (count two-pair-hands)))
