package org.iproduct.iptpi.domain.position;

import org.iproduct.iptpi.domain.arduino.EncoderReadings;

import reactor.core.publisher.Flux;

public class PositionFactory {
	public static PositionsFlux createPositionFlux(Flux<EncoderReadings> flux) {
		return new PositionsFlux(flux);
	}
	public static PositionPanel createPositionPanel(PositionsFlux fluxion) {
		PositionPanel positionPanel = new PositionPanel();
		fluxion.subscribe(positionPanel);
		return positionPanel;
	}
}
