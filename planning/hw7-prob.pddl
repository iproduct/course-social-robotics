;; hw7-prob.pddl

(define (problem hw7-prob)
  (:domain hw7)
  (:objects car self sister brother home concerthall park)
  (:init (at car home) ...)
  (:goal (and (at sister concerthall)
              ...)))
