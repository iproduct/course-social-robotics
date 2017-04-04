package demos;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class ReactorSimpleHello {

	public static void main(String[] args) throws InterruptedException {
		
		Flux.just("Learn React Flux", 
				"Learn React Mono", 
				"Program the Robot", 
				"Connect with Arduino",
				"React IPC with Netty").zipWith(
				Flux.intervalMillis(1000))
			.map(tpl -> "TODO "+ tpl.getT2() + ": "+ tpl.getT1())
//			.filter(m -> m.contains("React"))
//			.take(2)
			.subscribe(System.out::println);
		
//		Flux.intervalMillis(100).subscribe(System.out::println);
		
		Thread.sleep(5000);
	}

}
