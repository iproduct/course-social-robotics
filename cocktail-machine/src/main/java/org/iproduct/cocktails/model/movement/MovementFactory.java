package org.iproduct.cocktails.model.movement;
import org.iproduct.cocktails.model.arduino.LineReadings;
import org.iproduct.cocktails.model.audio.AudioPlayer;
import org.iproduct.cocktails.model.position.PositionsFlux;

import reactor.core.publisher.Flux;

public class MovementFactory {
	
	public static MovementCommandSubscriber createMovementCommandSubscriber(
			PositionsFlux positions,
			Flux<LineReadings> lineReadings
			) {
		return new MovementCommandSubscriber(positions, lineReadings);
	}
} 
