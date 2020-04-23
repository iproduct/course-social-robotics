/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiGpioExample.java  
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
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.wiringpi.SoftPwm;

public class LEDDemo01 {
	public static final int LED  = 5;

	public static void main(String[] args) throws InterruptedException {

		System.out.println("LED Demo started.");
		// initialize wiringPi library
		Gpio.wiringPiSetupGpio();

		// GPIO output pin
		Gpio.pinMode(LED, Gpio.OUTPUT);

		for (int i = 0; i < 10; i++) {
			Gpio.digitalWrite(LED, true);
			Thread.sleep(1000);
			Gpio.digitalWrite(LED, false);
			Thread.sleep(1000);
		}
		Gpio.digitalWrite(LED, false);
		System.out.println("LED Demo finished.");

		System.out.println("End of the demo.");

	}
}
