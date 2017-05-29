;; problem file: blocksworld-prob1.pddl

(define (problem blocksworld-prob1)
  (:domain blocksworld)
  (:objects b1 b2 b3 b4 b5 robot)
  (:init (on-table b1) (on b2 b1) (clear b2) (on-table b3) (on b4 b3)  (on b5 b4) (clear b5) (free robot))
  (:goal (and (on b3 b4) (on b5 b1) (free robot))))
                      