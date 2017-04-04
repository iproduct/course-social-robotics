package demos;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class ReactorSimpleTodo {

	
	public static void main(String[] args) throws InterruptedException {
		Todo[] todos = {
				new Todo("Learn React Flux", 45),
				new Todo("Learn React Mono", 20),
				new Todo("Program the Robot", 45), 
				new Todo("Connect with Arduino", 45),
				new Todo("React IPC with Netty", 45)
		};
		
//		Flux.fromArray(todos).zipWith(
//				Flux.intervalMillis(1000))
//			.map(tpl -> "TODO "+ tpl.getT2() + ": "+ tpl.getT1().getTitle() 
//					+ " - " + tpl.getT1().getMinutes())
////			.filter(m -> m.contains("React"))
////			.take(2)
//			.subscribe(System.out::println);
		
//		Flux.intervalMillis(100).subscribe(System.out::println);
		
		Flux.fromArray(todos)
			.zipWith(Flux.intervalMillis(1000))
			.map(tpl -> tpl.getT1().getMinutes())
			.scan(0, (accum, val) -> accum + val)
			.subscribe(total -> System.out.println("Total time [minutes]: " + total));
		
		Thread.sleep(5000);
	}

}
