package org.iproduct.cocktails.controller;

import static org.iproduct.iptpi.domain.CommandName.FOLLOW_LINE;
import static org.iproduct.iptpi.domain.CommandName.MOVE_RELATIVE;
import static org.iproduct.iptpi.domain.CommandName.SAY_HELLO;
import static org.iproduct.iptpi.domain.CommandName.STOP;

import java.util.function.Consumer;

import org.iproduct.iptpi.domain.Command;
import org.iproduct.iptpi.domain.arduino.ArduinoCommand;
import org.iproduct.iptpi.domain.arduino.ArduinoCommandSubscriber;
import org.iproduct.iptpi.domain.arduino.EncoderReadings;
import org.iproduct.iptpi.domain.audio.AudioPlayer;
import org.iproduct.iptpi.domain.movement.ForwardMovement;
import org.iproduct.iptpi.domain.movement.MovementCommandSubscriber;
import org.iproduct.iptpi.domain.movement.RelativeMovement;

import reactor.core.publisher.BlockingSink;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Schedulers;

public class RobotController {
	
	private final EmitterProcessor<Command> commandsFlux = EmitterProcessor.create();
	private final BlockingSink<Command> commandsSink = commandsFlux.connectSink();
	
	private final EmitterProcessor<ArduinoCommand> arduinoCommandsFlux = EmitterProcessor.create();
	private final BlockingSink<ArduinoCommand> arduinoCommandsSink = arduinoCommandsFlux.connectSink();
	
	private Consumer<Integer> onExitSubscriber;
	private AudioPlayer audio;
		
	public RobotController(Consumer<Integer> onExitSubscriber, MovementCommandSubscriber movementSub,
			ArduinoCommandSubscriber arduinoSub, AudioPlayer audio) {
		this.onExitSubscriber = onExitSubscriber;
		commandsFlux.subscribeOn(Schedulers.parallel()).subscribe(movementSub);
		arduinoCommandsFlux.subscribe(arduinoSub);
		this.audio = audio;
		
		// Just in case the robot is moving - stop it
		stop();
	}

	public void sayHello() {
		System.out.println("Saying I'm IPTPI in Bulgarian :)");
		audio.play();
		System.out.println("Message play finished.");
	}

	public void stop() {
		commandsSink.next(new Command(STOP, null));
		arduinoCommandsSink.next(ArduinoCommand.NOT_FOLLOW_LINE);
	}

	public void moveUp() {
		commandsSink.next(new Command(MOVE_RELATIVE, new RelativeMovement(400, 0, 0,  50)));
	}

	public void moveDown() {
		commandsSink.next(new Command(MOVE_RELATIVE, new RelativeMovement(-200, 0, 0,  -50)));
	}

	public void moveLeft() {
		commandsSink.next(new Command(MOVE_RELATIVE, new RelativeMovement(400, 0, 1.6f,  40)));
	}

	public void moveRight() {
		commandsSink.next(new Command(MOVE_RELATIVE, new RelativeMovement(400, 0, -1.6f,  40)));
	}
	
	public void followLine() {
		commandsSink.next(new Command(FOLLOW_LINE, new ForwardMovement(400, 50)));
		arduinoCommandsSink.next(ArduinoCommand.FOLLOW_LINE);
	}

	public void exit() {
		Mono.just(0).subscribe(onExitSubscriber);
		
	}

}
