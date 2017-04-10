package org.iproduct.cocktails.pump;

import static com.pi4j.io.gpio.PinState.LOW;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class PumpMotorSubscriber implements Subscriber<PinState> {
	private final Pin pin;
	private final GpioController gpio;
	private final GpioPinDigitalOutput gpioOutput;
	private Subscription subscription;
	
	// private SchedulerGroup eventLoops = SchedulerGroup.async();

	public PumpMotorSubscriber(GpioController gpioController, Pin motorOutPin) {
		gpio = gpioController;
		pin = motorOutPin;

		gpioOutput = gpio.provisionDigitalOutputPin(pin, LOW);
	}

	@Override
	public void onSubscribe(Subscription s) {
		subscription = s;
		subscription.request(Long.MAX_VALUE);
	}

	@Override
	public void onError(Throwable t) {
		t.printStackTrace(System.err);
	}

	@Override
	public void onComplete() {
		System.out.println("PumpMotorSubscriber on PIN="  + pin + " completed work successfully."); 
	}

	@Override
	public void onNext(PinState targetState) {
		gpioOutput.setState(targetState);

	}

}