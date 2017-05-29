;; domain file: blocksworld-domain.pddl

(define (domain blocksworld)
  (:requirements :strips)

  (:predicates (clear ?x)
               (on-table ?x)
               (holding ?x)
               (on ?x ?y)
               (free ?x)
               (block ?x)
               (robot ?x))

  (:action pickup
           :parameters (?ob ?robot)
           :precondition (and (clear ?ob) (on-table ?ob) (free ?robot) (block ?ob) (robot ?robot))
           :effect (and (holding ?ob) (not (free ?robot)) (not (clear ?ob)) (not (on-table ?ob))))

  (:action putdown
           :parameters (?ob ?robot)
           :precondition (and (holding ?ob) (block ?ob) (robot ?robot))
           :effect (and (clear ?ob) (on-table ?ob) (free ?robot)
                        (not (holding ?ob)) ))

  (:action stack
           :parameters (?ob ?underob ?robot)
           :precondition (and  (clear ?underob) (holding ?ob) (block ?ob) (block ?underob) (robot ?robot))
           :effect (and (clear ?ob) (on ?ob ?underob) (free ?robot)
                        (not (clear ?underob)) (not (holding ?ob)) ))

  (:action unstack
           :parameters (?ob ?underob ?robot)
           :precondition (and (on ?ob ?underob) (clear ?ob) (free ?robot) (block ?ob) (block ?underob) (robot ?robot))
           :effect (and (holding ?ob) (clear ?underob)
                        (not (on ?ob ?underob)) (not (clear ?ob)) (not (free ?robot)) )))
                        