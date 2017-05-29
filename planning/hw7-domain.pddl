;; hw7-domain.pddl

(define (domain hw7)
  (:requirements :strips)
  (:constants car self)
  (:predicates (at ?x ?y)
               ...)
  (:action goto
           :parameters (?from ?to)
           :precondition (and (at car ?from)
                              (in self car))
           :effect ...)

  ...)
