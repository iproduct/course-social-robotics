package org.iproduct.cocktails.model.position;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.iproduct.cocktails.robot.RobotParametrs.ENCODER_STEP_LENGTH;
import static org.iproduct.cocktails.robot.RobotParametrs.MAIN_AXE_LENGTH;

import org.iproduct.cocktails.model.arduino.EncoderReadings;
import org.reactivestreams.Subscriber;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

public class PositionsFlux extends  Flux<Position>{
	private Flux<Position> positionsFlux;
	private Flux<EncoderReadings> encoderReadings;
	private PositionPanel panel =  new PositionPanel();
	private Position currentPosition;
	EmitterProcessor<Position> positionsEmitter;
	SynchronousSink<Position> positionsSink;
	
	public PositionsFlux(Flux<EncoderReadings> readingsFlux) {
		this.encoderReadings = readingsFlux;
		
		Flux<EncoderReadings> skip1 = readingsFlux.skip(1);
		positionsFlux = Flux.zip(readingsFlux, skip1)
			.map(tupple -> {
//				System.out.println(tupple); 
				return tupple;
			})
			.scan(new Position(0, 0, 0), (last, tupple) -> {
				EncoderReadings prev = tupple.getT1();
				EncoderReadings curr = tupple.getT2();
				int prevL = prev.getEncoderL();
				int prevR = prev.getEncoderR();
				int currL = curr.getEncoderL();
				int currR = curr.getEncoderR();
				int sL = currL - prevL;
				int sR = currR - prevR;
				double alpha0 = last.getHeading();
				if(sR == sL) {
					return new Position((float)(last.getX() + sL * ENCODER_STEP_LENGTH * cos(alpha0)), 
							(float)(last.getY()+ sL * ENCODER_STEP_LENGTH * sin(alpha0)), alpha0, curr.getTimestamp());
				} else {
					double x = last.getX() + (MAIN_AXE_LENGTH * (sL+sR) * 
							(sin((sR - sL) * ENCODER_STEP_LENGTH /MAIN_AXE_LENGTH  + alpha0) - sin(alpha0)))
							/ (2 * (sR - sL));
					double y = last.getY() - (MAIN_AXE_LENGTH * (sL+sR) * 
							(cos((sR - sL) * ENCODER_STEP_LENGTH /MAIN_AXE_LENGTH  + alpha0) - cos(alpha0)))
							/ (2 * (sR - sL));
					double alpha = alpha0 + (sR - sL) * ENCODER_STEP_LENGTH / MAIN_AXE_LENGTH;
					return new Position((float)x, (float)y, alpha, curr.getTimestamp());
				}
			}).map(pos -> {
//				System.out.println("    #########   " + pos); 
				return pos;
			});
	}
	
	public PositionPanel getPresentationComponent() {
		return panel;
	}

	public Position getReport() {
		return currentPosition;
	}

	
	public void subscribe(Subscriber<? super Position> s) {
		positionsFlux.subscribe(s);	
	}

}
