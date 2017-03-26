package org.iproduct.iptpi.domain.arduino;

import java.io.IOException;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

public class ArduinoFactory {
	//Arduino serial device port
	public static final String PORT = "/dev/ttyACM0";
    // create an instance of the serial communications class
    private static final Serial SERIAL = SerialFactory.createInstance();
    private static ArduinoFactory instance = new ArduinoFactory();
    
    private ArduinoFactory(){
	    try {
			SERIAL.open(PORT, 38400);
	    } catch(SerialPortException | IOException ex) {
	        System.out.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
	    }
    }

    public static ArduinoFactory getInstance(){
    	return instance;
    }

	public ArduinoData createArduinoData() throws IOException {
		if ( !SERIAL.isOpen() )
			throw new IOException("Arduino serial not opened. Can not create input data fluxion.");
		return new ArduinoData(SERIAL);
	}
	
	public ArduinoCommandSubscriber createArduinoCommandSubscriber() throws IOException {
		if ( !SERIAL.isOpen() )
			throw new IOException("Arduino serial not opened. Can not create command subscriber.");
		return new ArduinoCommandSubscriber(SERIAL);
	}
} 
