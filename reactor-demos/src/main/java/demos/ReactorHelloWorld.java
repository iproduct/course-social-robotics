package demos;

import reactor.core.publisher.BlockingSink;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.scheduler.Schedulers;

public class ReactorHelloWorld {

	public static void main(String... args) throws InterruptedException {
		EmitterProcessor<String> emitter = EmitterProcessor.create();
		BlockingSink<String> sink = emitter.connectSink();
		emitter.publishOn(Schedulers.single()).map(String::toUpperCase).filter(s -> s.startsWith("HELLO"))
			.delayMillis(1000).subscribe(System.out::println);
		sink.submit("Hello World!"); // emit - non blocking
		sink.submit("Goodbye World!");
		sink.submit("Hello Trayan!"); // emit - non blocking
		
		Thread.sleep(3000);
	}
}
