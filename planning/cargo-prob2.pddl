(define (problem cargo)
  (:domain Cargo)
  (:objects c1 c2 sofia varna plovdiv p1 p2)

  (:init (airport sofia) (airport varna) (airport plovdiv)
         (plane p1) (plane p2)
         (at-airport p1 sofia) (at-airport p2 varna)
         (cargo c1) (cargo c2) (out c1) (out c2)
         (at-airport c1 sofia) (at-airport c2 plovdiv))

  (:goal (and (at-airport c1 varna) (out c1) (at-airport c2 varna) (out c2))))
