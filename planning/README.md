# STRIPS PLanning Demo

We are using use [JavaFF](https://nms.kcl.ac.uk/planning/software/javaff.html). You can download it as ZIP: [javaff.zip](http://cse3521.artifice.cc/downloads/javaff.zip).

## JavaFF

Java FF is a Java implementation of [FF](https://fai.cs.uni-saarland.de/hoffmann/ff.html) built to a clear object-oriented design to allow students to modify a working planner and study the results of doing so.

JavaFF is released under the GNU GPL. To download a .tar.gz file containing the source code and compiled Java bytecode, click the following link:

[JavaFF 1.0.1](https://nms.kcl.ac.uk/JavaFF/JavaFF.tar.gz) (N.B. This is the original code that has been superseded by a later version, see below)

To run JavaFF, expand the archive, set the CLASSPATH environment variable to point to the directory in which it was expanded, then type:

```
java javaff.JavaFF domain.pddl problem.pddl
```

To allocate more memory, beyond Java's default, you may wish to type:

```
java -Xmx1024m javaff.JavaFF domain.pddl problem.pddl
```

Thanks to David Pattison for pointing out a bug in JavaFF affecting relaxed plan generation in non-temporal, non-numeric domains. A later version with this issue resolved is available from [his website](http://personal.strath.ac.uk/david.pattison/).

## FF
Fast-Forward, abbreviated FF, is a domain independent planning system developed by Joerg. FF can handle classical STRIPS- as well as full scale ADL- planning tasks, to be specified in PDDL (for the version that can handle numerical state variables on top of that, check out the other page). The system is implemented in C. It has competed in the fully automated track of the 2nd International Planning Competition. As a result of the competition, FF was granted ``Group A distinguished performance Planning System'', and it also won the Schindler Award for the best performing planning system in the Miconic 10 Elevator domain, ADL track. The system (slightly de-bugged) also participated in the 3rd International Planning Competition where it excelled in the STRIPS domains (but didn't get an award because of the broader language coverage of other competitive systems). Check out our web page providing gnuplots of the runtime and solution length data in the STRIPS (and Numerical) domains used in the competition. On the page at hand we make available the source code used in the 3rd International Planning Competition, and some older source code for an easier readable STRIPS version. We also give pointers to publications relevant for the system, and provide some interesting information on what makes the system so efficient across many benchmark domains.

## PDDL 
### Example: Blocks world

#### Domain file:
```
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
```

#### Problem file

```
;; problem file: blocksworld-prob1.pddl

(define (problem blocksworld-prob1)
  (:domain blocksworld)
  (:objects a b)
  (:init (on-table a) (on-table b) (clear a) (clear b))
  (:goal (and (on a b))))
```

#### Run planner

Use this command:

```
java -cp . javaff.JavaFF blocksworld-domain.pddl blocksworld-prob1.pddl
```

You should see this output:

```
Parsed Domain file blocksworld-domain.pddl successfully
Parsed Problem file blocksworld-prob1.pddl successfully
Performing search as in FF - first considering EHC with only helpful actions
2
1
(pickup a)
(stack a b)
Instantiation Time =            0.07sec
Planning Time = 0.03sec
```
