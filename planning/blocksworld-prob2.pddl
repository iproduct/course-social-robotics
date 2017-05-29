;; problem file: blocksworld-prob1.pddl

(define (problem blocksworld-prob1)
  (:domain blocksworld)
  (:objects b1 b2 b3 b4 b5 robot1)
  (:init (on-table b1) (on b2 b1) (clear b2) (on-table b3) (on b4 b3)  (on b5 b4) (clear b5) (free robot1)
  			(block b1) (block b2) (block b3) (block b4) (block b5) (robot robot1))
  (:goal (and (on b3 b4) (on b5 b1) )))
                      