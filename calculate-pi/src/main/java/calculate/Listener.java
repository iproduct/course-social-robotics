package calculate;

import akka.actor.AbstractActor;
import akka.io.Tcp.Message;
import calculate.CalculatePi.PiApproximation;

public class Listener extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(PiApproximation.class, message -> {
			System.out.println(String.format(
					"\nPi approximation: \t\t%s\nCalculation time: \t %s",
					message.getPi(), message.getDuration()));
			//Finish demo
			getContext().system().terminate();
		}).build();
	}

}
