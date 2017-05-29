;; problem file: blocksworld-prob1.pddl

(define (problem blocksworld-prob1)
  (:domain blocksworld)
  (:objects a b robot)
  (:init (on-table a) (on-table b) (clear a) (clear b) (free robot))
  (:goal (and (on a b))))
                          