package calculate;

import akka.actor.AbstractActor;
import calculate.CalculatePi.Result;
import calculate.CalculatePi.Work;

public class Worker extends AbstractActor{
	
	

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Work.class, work -> {
			double result = calculatePiPart(work.getStart(), work.getNrOfElements());
			getSender().tell(new Result(result), self());
		}).build();
	}

private double calculatePiPart(int start, int nrOfElements) {
	double acc = 0.0;
	for(int i = start * nrOfElements; i < (start+1) * nrOfElements - 1; i++) {
		acc += 4.0 * (1 - (i % 2) * 2 ) / (2 * i + 1);
	}
	return acc;
	
}


}
