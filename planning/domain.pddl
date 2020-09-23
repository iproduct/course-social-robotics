(define (domain planeDomain)
  (:requirements :strips)

  (:predicates (plane ?x)
               (airport ?x)
               (planeAt-airport ?x ?y))
               
  (:action fly
           :parameters (?plane ?from ?to)
           :precondition (and (plane ?plane) (airport ?to) (airport ?from) (planeAt-airport ?plane ?from))
           :effect (and (planeAt-airport ?plane ?to) (not (planeAt-airport ?plane ?from))))
          
)