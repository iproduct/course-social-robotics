/*
 * **********************************************************************
 * ORGANIZATION  :  IPT - Intellectual Products & Technologies
 * PROJECT       :  IPTPI Examples
 * FILENAME      :  IPTMotorDemo01.java  
 * 
 * This file is part of the IPTPI project. More information about 
 * this project can be found here:  http://iproduct.org/
 * **********************************************************************
 * 
 * Copyright (C) 2015 IPT - Intellectual Products & Technologies Ltd.
 * 
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
 * 
 */
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.wiringpi.SoftPwm;

public class MotorDemo01 {

	public static void main(String[] args) throws InterruptedException {

		// initialize wiringPi library
		Gpio.wiringPiSetupGpio();
		
		// Motor direction pins
		Gpio.pinMode(5, Gpio.OUTPUT);
		Gpio.pinMode(6, Gpio.OUTPUT);

		Gpio.pinMode(12, Gpio.PWM_OUTPUT);
		Gpio.pinMode(13, Gpio.PWM_OUTPUT);
		Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
		Gpio.pwmSetRange(480);
		Gpio.pwmSetClock(2);


		System.out.println("Turning left");
		//setting motor directions 
		Gpio.digitalWrite(5, 1);
		Gpio.digitalWrite(6, 0);
		//setting speed
		Gpio.pwmWrite(12, 460); // speed 460 of 480 max
		Gpio.pwmWrite(13, 460);
		// turn duration
		Thread.sleep(3000);

//
		System.out.println("Turning right");
		//setting motor directions 
		Gpio.digitalWrite(5, 1);
		Gpio.digitalWrite(6, 0);
		//setting speed
		Gpio.pwmWrite(12, 460); // speed 460 of 480 max
		Gpio.pwmWrite(13, 460);
		// turn duration
		Thread.sleep(3000);

		System.out.println("Running motors forward accelerating");

		for (int i = 0; i <= 480; i++) {
			//setting motor directions 
			Gpio.digitalWrite(5, 1);
			Gpio.digitalWrite(6, 1);
			//setting speed
			Gpio.pwmWrite(12, i); 
			Gpio.pwmWrite(13, i);
			Thread.sleep(40);
		}

		System.out.println("Running motors forward decelerating");
		for (int i = 480; i > 0; i--) {
			//setting motor directions 
			Gpio.digitalWrite(5, 1);
			Gpio.digitalWrite(6, 1);
			//setting speed
			Gpio.pwmWrite(12, i);
			Gpio.pwmWrite(13, i);
			Thread.sleep(20);
		}

		//setting motor directions 
		Gpio.digitalWrite(5, 0);
		Gpio.digitalWrite(6, 1);
		//setting speed
		Gpio.pwmWrite(12, 240);
		Gpio.pwmWrite(13, 240);
		Thread.sleep(5000);

		System.out.println("Running motors backwards decelerating");
		for (int i = 480; i > 0; i--) {
			//setting motor directions 
			Gpio.digitalWrite(5, 0);
			Gpio.digitalWrite(6, 0);
			//setting speed
			Gpio.pwmWrite(12, i);
			Gpio.pwmWrite(13, i);
			Thread.sleep(40);
		}

		// turning the motors off
		Gpio.digitalWrite(5, 0);
		Gpio.digitalWrite(6, 0);
		Gpio.pwmWrite(12, 0);
		Gpio.pwmWrite(13, 0);

		System.out.println("End of the demo.");

	}
}
