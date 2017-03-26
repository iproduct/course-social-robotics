package org.iproduct.iptpi.domain.movement;
import org.iproduct.iptpi.domain.arduino.LineReadings;
import org.iproduct.iptpi.domain.audio.AudioPlayer;
import org.iproduct.iptpi.domain.position.PositionsFlux;

import reactor.core.publisher.Flux;

public class MovementFactory {
	
	public static MovementCommandSubscriber createMovementCommandSubscriber(
			PositionsFlux positions,
			Flux<LineReadings> lineReadings
			) {
		return new MovementCommandSubscriber(positions, lineReadings);
	}
} 
