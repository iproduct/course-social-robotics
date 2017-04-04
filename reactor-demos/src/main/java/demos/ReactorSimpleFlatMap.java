package demos;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class ReactorSimpleFlatMap {

	
	public static void main(String[] args) throws InterruptedException {

		Flux.range(0, 10)
			.take(5)
			.map(v -> v * 3)
			.flatMap(v -> Flux.range(v, 3))
			.subscribe(System.out::println);
		
		Thread.sleep(5000);
	}

}
