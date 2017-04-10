package org.iproduct.cocktails.flowmeter;
// START SNIPPET: serial-snippet

import org.reactivestreams.Subscriber;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import reactor.core.publisher.BlockingSink;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

/**
 * This class represents an event stream (Fluxion in Reactor terms) providing
 * fluent API for event transformations and allowing all interested consumenrs
 * to listen for EncoderReadings received from Arduino Leonardo USB connected
 * microcontroller.
 * 
 * @author Trayan Iliev
 */
public class FlowMeterFlux extends Flux<Double> {
	private static final double MILILITERS_PER_IMPULS = 0.0850340136;
	private final EmitterProcessor<Double> flowMeterEmitter;
	private final BlockingSink<Double> flowMeterSink;
	private final Pin pin;
	private final GpioController gpio;
	private final GpioPinDigitalInput gpioInput;
//	private volatile int counter = 0;

	public FlowMeterFlux(GpioController gpioController, Pin flowMeterPin) {
		this.gpio = gpioController;
		this.pin = flowMeterPin;

		flowMeterEmitter = EmitterProcessor.create();
		flowMeterSink = flowMeterEmitter.connectSink();

		gpioInput = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
		gpioInput.addListener(new GpioPinListenerDigital() {
			long counter = 0L;
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				counter++;
//				System.out.println(
//						"--> GPIO PIN STATE CHANGE " + event.getPin() + " " + event.getState() + " --> " + counter);
//				if (flowMeterSink.requestedFromDownstream() > 0)
//					System.out.println("--> " + flowMeterSink.emit(counter * MILILITERS_PER_IMPULS));
					flowMeterSink.emit(counter * MILILITERS_PER_IMPULS);
//				else
//					System.err.println("Error: Missed FlowMeter reading because of missing subscriber request.");
			}
		});
	}

	@Override
	public void subscribe(Subscriber<? super Double> consumer) {
		flowMeterEmitter.subscribe(consumer);
	}
}
