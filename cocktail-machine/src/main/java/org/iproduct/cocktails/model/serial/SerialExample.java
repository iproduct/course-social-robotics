package org.iproduct.cocktails.model.serial;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import com.pi4j.wiringpi.Gpio;

/**
 * This example code demonstrates how to perform serial communications using the
 * Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class SerialExample {

	public static final String PORT = "/dev/ttyAMA0";
	private static final Scanner sc = new Scanner(System.in);

	public static void main(String args[]) throws InterruptedException {

		// !! ATTENTION !!
		// By default, the serial port is configured as a console port
		// for interacting with the Linux OS shell. If you want to use
		// the serial port in a software program, you must disable the
		// OS from using this port. Please see this blog article by
		// Clayton Smith for step-by-step instructions on how to disable
		// the OS console for this port:
		// http://www.irrational.net/2012/04/19/using-the-raspberry-pis-serial-port/

		// initialize wiringPi library
		Gpio.wiringPiSetupGpio();

		// GPIO output pin
		Gpio.pinMode(14, Gpio.OUTPUT);
		Gpio.digitalWrite(14, false);

		System.out.println("<--Pi4J--> Serial Communication Example ... started.");
		System.out.println(" ... connect using settings: 9600, N, 8, 1.");
		System.out.println(" ... data received on serial port should be displayed below.");

		// create an instance of the serial communications class
		final Serial serial = SerialFactory.createInstance();
		SerialConfig conf = new SerialConfig();
		conf.baud(Baud._9600);	
		conf.device(Serial.DEFAULT_COM_PORT);
		conf.flowControl(FlowControl.NONE);
		conf.dataBits(DataBits._8);
		conf.stopBits(StopBits._1);
		conf.parity(Parity.NONE);

		// create and register the serial data listener
		SerialDataEventListener listener = new SerialDataEventListener() {

			@Override
			public void dataReceived(SerialDataEvent event) {
				// print out the data received to the console
				byte[] reading;
				try {
					reading = event.getBytes();
					System.out.println(new String(reading, Charset.forName("UTF-8")));
//					System.out.println(Arrays.toString(reading));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		serial.addListener(listener);

		try {
			// open the default serial port provided on the GPIO header
			serial.open(conf);
			System.out.println("Demo running ... ");
			
			for (int i = 0; i < 50; i++) {
				Gpio.digitalWrite(14, true);
				Thread.sleep(40);
				Gpio.digitalWrite(14, false);
				Thread.sleep(500);
			}
			serial.discardInput();
			serial.removeListener(listener);
			serial.close();
			System.out.println("Demo finished.");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("End of program");
			System.exit(0);
		}
	}
}

