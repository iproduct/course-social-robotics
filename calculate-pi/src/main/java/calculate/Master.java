package calculate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.xml.bind.Marshaller.Listener;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
import calculate.CalculatePi.*;

public class Master extends UntypedAbstractActor {
	private final int nrOfElements;
	private final int nrOfMessages;
	private final ActorRef listener;

	private double pi = 0;
	private int nrOfResults;
	private final long startTime = System.currentTimeMillis();

	private final ActorRef workerRouter;

	public Master(final int nrOfWorkers, int nrOfMessages, int nrOfElements, ActorRef listener) {
		this.nrOfElements = nrOfElements;
		this.nrOfMessages = nrOfMessages;
		this.listener = listener;

		workerRouter = getContext()
				.actorOf(Props.create(Worker.class)
				.withRouter(new RoundRobinPool(nrOfWorkers)),
				"workerRouter");

	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Calculate) {
			for (int i = 0; i < nrOfMessages; i++) {
				workerRouter.tell(new Work(i, nrOfElements), self());
			}
		} else if( message instanceof Result) {
			Result result = (Result) message;
			pi += result.getValue();
			nrOfResults ++;
			if (nrOfResults == nrOfMessages) {
				// send result to top level listener
				Duration duration = 
					Duration.of(System.currentTimeMillis() - startTime, ChronoUnit.MILLIS);
				listener.tell(new PiApproximation(pi, duration), self());
			}
		} else {
			unhandled(message);
		}

	}

}
