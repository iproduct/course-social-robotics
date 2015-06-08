package akka.tutorial.first.java.akka.tutorial.first.java;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;


/** From Akka tutorial, 
 *  with modifications - become/unbecome 
 *  for Listener actor
 */
public class Pi {
	// Messages
	static class Calculate {
	}

	static class UpgradeBehavior {
	}

	static class DowngradeBehavior {
	}

	static class Work {
		private final int start;
		private final int nrOfElements;

		public Work(int start, int nrOfElements) {
			this.start = start;
			this.nrOfElements = nrOfElements;
		}

		public int getStart() {
			return start;
		}

		public int getNrOfElements() {
			return nrOfElements;
		}
	}

	static class Result {
		private final double value;

		public Result(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}
	}

	static class PiApproximation {
		private final double pi;
		private final Duration duration;

		public PiApproximation(double pi, Duration duration) {
			this.pi = pi;
			this.duration = duration;
		}

		public double getPi() {
			return pi;
		}

		public Duration getDuration() {
			return duration;
		}
	}

	// Workers
	public static class Worker extends UntypedActor {

		private double calculatePiFor(int start, int nrOfElements) {
			double acc = 0.0;
			for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
				acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
			}
			return acc;
		}

		public void onReceive(Object message) {
			if (message instanceof Work) {
				Work work = (Work) message;
				double result = calculatePiFor(work.getStart(),
						work.getNrOfElements());
				getSender().tell(new Result(result), getSelf());
			} else {
				unhandled(message);
			}
		}
	}

	public static class Master extends UntypedActor {
		private final int nrOfMessages;
		private final int nrOfElements;

		private double pi;
		private int nrOfResults;
		private final long start = System.currentTimeMillis();

		private final ActorRef listener;
		private final ActorRef workerRouter;

		public Master(final int nrOfWorkers, int nrOfMessages,
				int nrOfElements, ActorRef listener) {
			this.nrOfMessages = nrOfMessages;
			this.nrOfElements = nrOfElements;
			this.listener = listener;

			workerRouter = this.getContext().actorOf(
					Props.create(Worker.class).withRouter(
							new RoundRobinPool(nrOfWorkers)), "workerRouter");
		}

		public void onReceive(Object message) {
			if (message instanceof Calculate) {
				for (int start = 0; start < nrOfMessages; start++) {
					workerRouter.tell(new Work(start, nrOfElements), getSelf());
				}
			} else if (message instanceof Result) {
				Result result = (Result) message;
				pi += result.getValue();
				nrOfResults += 1;
				if (nrOfResults == nrOfMessages) {
					// Send the result to the listener
					Duration duration = Duration.of(System.currentTimeMillis()
							- start, ChronoUnit.MILLIS);
					listener.tell(new PiApproximation(pi, duration), getSelf());
					// Stops this actor and all its supervised children
					getContext().stop(getSelf());
				}
			} else {
				unhandled(message);
			}
		}
	}

	public static class Listener extends UntypedActor {
		public void onReceive(Object message) {
			if (message instanceof PiApproximation) {
				PiApproximation approximation = (PiApproximation) message;
				System.out
						.println(String
								.format("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s",
										approximation.getPi(),
										approximation.getDuration()));
				getContext().system().shutdown();
			} else if (message instanceof UpgradeBehavior) {
				System.out.println("Upgrading");

				getContext()
						.become((m) -> {
							if (m instanceof PiApproximation) {
								PiApproximation approximation = (PiApproximation) m;
								System.out
										.println(String
												.format("\n\tUpgraded Pi approximation: \t\t%s\n\tCalculation time: \t%s",
														approximation.getPi(),
														approximation
																.getDuration()));
								getContext().system().shutdown();
							} else if (m instanceof DowngradeBehavior) {
								System.out.println("Downgrading");
								getContext().unbecome();
							} else {
								unhandled(m);
							}
						}, false);
			} else {
				unhandled(message);
			}
		}
	}

	public static void main(String[] args) {
		Pi pi = new Pi();
		pi.calculate(4, 10000, 10000);
	}

	public void calculate(final int nrOfWorkers, final int nrOfElements,
			final int nrOfMessages) {
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiSystem");

		// create the result listener, which will print the result and shutdown
		// the system
		final ActorRef listener = system.actorOf(Props.create(Listener.class),
				"listener");
		listener.tell(new UpgradeBehavior(), null);
		listener.tell(new DowngradeBehavior(), null);

		// create the master
		ActorRef master = system.actorOf(Props.create(Master.class,
				nrOfWorkers, nrOfMessages, nrOfElements, listener), "master");

		// start the calculation
		master.tell(new Calculate(), null);

	}
}
