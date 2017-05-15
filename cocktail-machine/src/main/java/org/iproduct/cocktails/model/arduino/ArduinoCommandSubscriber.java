package org.iproduct.cocktails.model.arduino;

import java.io.IOException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.pi4j.io.serial.Serial;

public class ArduinoCommandSubscriber implements Subscriber<ArduinoCommand>  {
	
	private Serial serial;
	private Subscription subscription;
	
	public ArduinoCommandSubscriber(Serial serial) {
		this.serial = serial;
	}

	@Override
	public void onNext(ArduinoCommand command) {
		switch (command) {
		case FOLLOW_LINE :  // enable line following sensors readings
		case NOT_FOLLOW_LINE :  // disable line following sensors readings
			try {
				serial.write(command.getCode());
			} catch (IllegalStateException | IOException ex) {
		        System.out.println(" ==>> ERROR SENDING SERIAL DATA TO ARDUINO : " + ex.getMessage());
			}
			break;
		}
	}

	@Override
	public void onSubscribe(Subscription s) {
		subscription = s;
		subscription.request(Long.MAX_VALUE);
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

}
