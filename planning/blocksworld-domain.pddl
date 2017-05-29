;; domain file: blocksworld-domain.pddl

(define (domain blocksworld)
  (:requirements :strips)

  (:predicates (clear ?x)
               (on-table ?x)
               (holding ?x)
               (on ?x ?y)
               (free ?x))

  (:action pickup
           :parameters (?ob ?robot)
           :precondition (and (clear ?ob) (on-table ?ob) (free ?robot))
           :effect (and (holding ?ob) (not (free ?robot)) (not (clear ?ob)) (not (on-table ?ob))))

  (:action putdown
           :parameters (?ob ?robot)
           :precondition (and (holding ?ob))
           :effect (and (clear ?ob) (on-table ?ob) (free ?robot)
                        (not (holding ?ob)) ))

  (:action stack
           :parameters (?ob ?underob ?robot)
           :precondition (and  (clear ?underob) (holding ?ob))
           :effect (and (clear ?ob) (on ?ob ?underob) (free ?robot)
                        (not (clear ?underob)) (not (holding ?ob)) ))

  (:action unstack
           :parameters (?ob ?underob ?robot)
           :precondition (and (on ?ob ?underob) (clear ?ob) (free ?robot))
           :effect (and (holding ?ob) (clear ?underob)
                        (not (on ?ob ?underob)) (not (clear ?ob)) (not (free ?robot)) )))
                        