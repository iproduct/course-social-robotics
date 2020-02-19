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

public class AudioDemo {

	public static void main(String[] args) throws InterruptedException {

		// initialize wiringPi library
		Gpio.wiringPiSetupGpio();

		// GPIO output pin
		Gpio.pinMode(17, Gpio.OUTPUT);
		Gpio.pullUpDnControl(17, Gpio.PUD_DOWN);
		Gpio.digitalWrite(17, false);

		for (int i = 0; i < 30; i++) {
			System.out.println("Iteration " + i);
			Gpio.digitalWrite(17, true);
			Thread.sleep(60000);
			Gpio.digitalWrite(17, false);
			Thread.sleep(500);
		}
		Gpio.digitalWrite(17, false);

		// Gpio.digitalWrite(6, 1);
		//
		// Gpio.pwmWrite(12, 240);
		// Gpio.pwmWrite(13, 240);
		// // SoftPwm.softPwmWrite(12, i);
		// Thread.sleep(5000);
		//
		// System.out.println("Running motors forward accelerating");
		//
		// for (int i = 0; i <= 480; i++) {
		// Gpio.digitalWrite(5, 1);
		// Gpio.digitalWrite(6, 1);
		//
		// Gpio.pwmWrite(12, i);
		// Gpio.pwmWrite(13, i);
		// // SoftPwm.softPwmWrite(12, i);
		// Thread.sleep(40);
		// }
		//
		// System.out.println("Running motors forward decelerating");
		// for (int i = 480; i > 0; i--) {
		// Gpio.digitalWrite(5, 1);
		// Gpio.digitalWrite(6, 1);
		//
		// Gpio.pwmWrite(12, i);
		// Gpio.pwmWrite(13, i);
		// // SoftPwm.softPwmWrite(12, i);
		// Thread.sleep(20);
		// }
		//
		// Gpio.digitalWrite(5, 0);
		// Gpio.digitalWrite(6, 1);
		//
		// Gpio.pwmWrite(12, 240);
		// Gpio.pwmWrite(13, 240);
		// // SoftPwm.softPwmWrite(12, i);
		// Thread.sleep(5000);
		//
		// System.out.println("Running motors forward decelerating");
		// for (int i = 480; i > 0; i--) {
		// Gpio.digitalWrite(5, 1);
		// Gpio.digitalWrite(6, 1);
		//
		// Gpio.pwmWrite(12, i);
		// Gpio.pwmWrite(13, i);
		// // SoftPwm.softPwmWrite(12, i);
		// Thread.sleep(50);
		// }
		//
		// Gpio.digitalWrite(5, 0);
		// Gpio.digitalWrite(6, 0);
		// Gpio.pwmWrite(12, 0);
		// Gpio.pwmWrite(13, 0);

		System.out.println("End of the demo.");

	}
}
