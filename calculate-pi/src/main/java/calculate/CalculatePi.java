package calculate;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class CalculatePi {
	
	static class Calculate {}
	
	static class Work {
		private final int start;
		private final int nrOfElements;
		public Work(int start, int nrOfElements) {
			super();
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
			super();
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
			super();
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

	public void calculatePi(final int nrOfWorkers, final int nrOfElements, final int nrOfMessages) {
		// Create top level actor system
//		akka.Main.main(new String[] {HelloWorld.class.getName()}); 
		ActorSystem system = ActorSystem.create("CalculatePiSystem");
		
		//create result listener
		ActorRef listener = system.actorOf(Props.create(Listener.class), "listener");
		
		// Create master 
		ActorRef master = system.actorOf(
				Props.create(Master.class, nrOfWorkers, nrOfMessages, nrOfElements, listener), "master");
		master.tell(new Calculate(), null);
	}

	public static void main(String[] args) {
		CalculatePi calculatePi = new CalculatePi();
		calculatePi.calculatePi(4, 10000, 10000);
	}

}
