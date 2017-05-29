;; probot1oblem file: blocksworld-prob1.pddl

(define (problem blocksworld-prob1)
  (:domain blocksworld)
  (:objects a b robot1)
  (:init (on-table a) (on-table b) (clear a) (clear b) (free robot1) (block a) (block b) (robot robot1))
  (:goal (and (on a b))))
                          