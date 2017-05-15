package org.iproduct.cocktails.controller;

import static org.iproduct.cocktails.model.CommandName.STOP;

import java.util.function.Consumer;

import org.iproduct.cocktails.model.Command;
import org.iproduct.cocktails.model.arduino.ArduinoCommand;
import org.iproduct.cocktails.model.arduino.ArduinoCommandSubscriber;
import org.iproduct.cocktails.model.arduino.EncoderReadings;
import org.iproduct.cocktails.model.audio.AudioPlayer;
import org.iproduct.cocktails.model.movement.ForwardMovement;
import org.iproduct.cocktails.model.movement.MovementCommandSubscriber;
import org.iproduct.cocktails.model.movement.RelativeMovement;

import reactor.core.publisher.BlockingSink;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Schedulers;

public class CocktailMachineController {
	
//	private final EmitterProcessor<Command> commandsFlux = EmitterProcessor.create();
//	private final BlockingSink<Command> commandsSink = commandsFlux.connectSink();
//	
//	private final EmitterProcessor<ArduinoCommand> arduinoCommandsFlux = EmitterProcessor.create();
//	private final BlockingSink<ArduinoCommand> arduinoCommandsSink = arduinoCommandsFlux.connectSink();
//	
//	private Consumer<Integer> onExitSubscriber;
//	private AudioPlayer audio;
		
	public CocktailMachineController() {}
	
	
//	public CocktailMachineController(Consumer<Integer> onExitSubscriber, MovementCommandSubscriber movementSub,
//			ArduinoCommandSubscriber arduinoSub, AudioPlayer audio) {
//		this.onExitSubscriber = onExitSubscriber;
//		commandsFlux.subscribeOn(Schedulers.parallel()).subscribe(movementSub);
//		arduinoCommandsFlux.subscribe(arduinoSub);
//		this.audio = audio;
//		
//		// Just in case the robot is moving - stop it
//		stop();
//	}

	public void makeCocktail() {
		System.out.println("Makeing cocktail ...");
	}
	
	public void stop() {
		System.out.println("Stopping ...");
	}

//	public void stop() {
//		commandsSink.next(new Command(STOP, null));
//		arduinoCommandsSink.next(ArduinoCommand.NOT_FOLLOW_LINE);
//	}

	public void exit() {
//		Mono.just(0).subscribe(onExitSubscriber);
		System.exit(0);
		
	}

}
