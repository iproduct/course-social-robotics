package org.iproduct.cocktails;
// START SNIPPET: serial-snippet

import org.iproduct.cocktails.model.Command;
import org.reactivestreams.Subscriber;

import reactor.core.publisher.BlockingSink;
import reactor.core.publisher.BlockingSink.Emission;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

/**
 * This class represents an event stream (Fluxion in Reactor terms) providing
 * fluent API for event transformations and allowing all interested consumenrs
 * to listen for new cocktail Command requests
 * 
 * @author Trayan Iliev
 */
public class CommandFlux extends Flux<Command> {
	private final EmitterProcessor<Command> commandEmitter;
	private final BlockingSink<Command> commandSink;

	public CommandFlux() {
		commandEmitter = EmitterProcessor.create();
		commandSink = commandEmitter.connectSink();
	}
	
	public Emission emit(Command command) {
		return commandSink.emit(command);
	}
	
	public long submit(Command command, long timeout) {
		return commandSink.submit(command, timeout);
	}

	@Override
	public void subscribe(Subscriber<? super Command> consumer) {
		commandEmitter.subscribe(consumer);
	}
}
