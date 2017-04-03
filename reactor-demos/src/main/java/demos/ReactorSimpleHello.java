package demos;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class ReactorSimpleHello {

	public ReactorSimpleHello() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws InterruptedException {
		String[] todos = {
				"Learn React Flux", 
				"Learn React Mono", 
				"Program the Robot", 
				"Connect with Arduino",
				"React IPC with Netty"
		};
		
		Flux.fromArray(todos).zipWith(
				Flux.intervalMillis(1000))
			.map(tpl -> "TODO "+ tpl.getT2() + ": "+ tpl.getT1())
//			.filter(m -> m.contains("React"))
//			.take(2)
			.subscribe(System.out::println);
		
//		Flux.intervalMillis(100).subscribe(System.out::println);
		
		Thread.sleep(5000);
	}

}
