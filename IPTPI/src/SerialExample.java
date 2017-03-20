// START SNIPPET: serial-snippet

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SerialExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;
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

		// create and register the serial data listener
		serial.addListener(new SerialDataListener() {

			@Override
			public void dataReceived(SerialDataEvent event) {
				// print out the data received to the console
				byte[] reading = event.getData().getBytes();
				for (int i = 0; i < reading.length; i++)
					System.out.println(Integer.toBinaryString(reading[i]) + "  ");
				System.out.println();
				System.out.println(new String(reading, Charset.forName("UTF-8")));
				System.out.println(Arrays.toString(reading));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		try {
			// open the default serial port provided on the GPIO header
			// serial.open(Serial.DEFAULT_COM_PORT, 38400);
			// try {
			serial.open(PORT, 9600);
			System.out.println("Demo running ... Press <ENTER> to finish.");
			sc.nextLine();

//			for (int i = 0; i < 100; i++) {
//				Gpio.digitalWrite(14, true);
//				Thread.sleep(40);
//				Gpio.digitalWrite(14, false);
//				Thread.sleep(2000);
//			}
			System.out.println("Demo finished.");
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// continuous loop to keep the program running until the user
			// terminates the program
			// for (;;) {
			// try {
			// // write a formatted string to the serial transmit buffer
			// serial.write("CURRENT TIME: %s", new Date().toString());
			//
			// // write a individual bytes to the serial transmit buffer
			// serial.write((byte) 13);
			// serial.write((byte) 10);
			//
			// // write a simple string to the serial transmit buffer
			// serial.write("Second Line");
			//
			// // write a individual characters to the serial transmit buffer
			// serial.write('\r');
			// serial.write('\n');
			//
			// // write a string terminating with CR+LF to the serial transmit
			// buffer
			// serial.writeln("Third Line");
			// }
			// catch(IllegalStateException ex){
			// ex.printStackTrace();
			// }
			//
			// // wait 1 second before continuing
			// Thread.sleep(1000);
			// }

		} catch (SerialPortException ex) {
			System.out.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
			return;
		}
	}
}

// END SNIPPET: serial-snippet
