(define (domain Cargo)
  (:requirements :strips)

  (:predicates (in ?c ?p) (out ?c) (at-airport ?x ?a)
               (plane ?p) (airport ?a) (cargo ?c))

  (:action load :parameters (?c ?p ?a)
           :precondition (and (at-airport ?c ?a) (at-airport ?p ?a) 
                              (out ?c) (plane ?p) (cargo ?c) (airport ?a))
           :effect (and (not (at-airport ?c ?a)) (in ?c ?p) (not (out ?c))))
  
  (:action unload :parameters (?c ?p ?a)
           :precondition (and (in ?c ?p) (at-airport ?p ?a)
                              (plane ?p) (cargo ?c) (airport ?a))
           :effect (and (not (in ?c ?p)) (at-airport ?c ?a) (out ?c)))
  
  (:action fly :parameters (?p ?from ?to)
           :precondition (and (at-airport ?p ?from) 
                              (plane ?p) (airport ?from) (airport ?to))
           :effect (and (not (at-airport ?p ?from)) (at-airport ?p ?to))))

