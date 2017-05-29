;; domain file: blocksworld-domain.pddl

(define (domain blocksworld)
  (:requirements :strips)

  (:predicates (clear ?x)
               (on-table ?x)
               (holding ?x)
               (on ?x ?y))

  (:action pickup
           :parameters (?ob)
           :precondition (and (clear ?ob) (on-table ?ob))
           :effect (and (holding ?ob) (not (clear ?ob)) (not (on-table ?ob))))

  (:action putdown
           :parameters (?ob)
           :precondition (and (holding ?ob))
           :effect (and (clear ?ob) (on-table ?ob) 
                        (not (holding ?ob))))

  (:action stack
           :parameters (?ob ?underob)
           :precondition (and  (clear ?underob) (holding ?ob))
           :effect (and (clear ?ob) (on ?ob ?underob)
                        (not (clear ?underob)) (not (holding ?ob))))

  (:action unstack
           :parameters (?ob ?underob)
           :precondition (and (on ?ob ?underob) (clear ?ob))
           :effect (and (holding ?ob) (clear ?underob)
                        (not (on ?ob ?underob)) (not (clear ?ob)))))
                        