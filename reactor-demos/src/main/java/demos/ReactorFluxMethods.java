package demos;

import java.util.Arrays;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorFluxMethods {

	public static void main(String... args) throws InterruptedException {
		Flux.fromIterable(Arrays.asList(1L, 2L, 3L, 0L, 5L, 6L, 7L, 8L, 9L)).mergeWith(Flux.intervalMillis(100))
				.doOnNext(d -> System.out.println("doOnNext(" + d + ")")).map(d -> 100 / d).take(5)
				.onErrorResumeWith(error -> {
					System.out.println("onErrorResumeWith(" + error + ")");
					return Mono.just(0L);
				}).doAfterTerminate(() -> System.out.println("doAfterTerminate()")).subscribe(System.out::println);

		Thread.sleep(3000);
	}
}
