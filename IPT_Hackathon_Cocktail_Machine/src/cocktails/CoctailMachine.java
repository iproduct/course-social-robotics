/* 
 *************************************************************************
 * ORGANIZATION  :  ROBOLEARN.ORG
 * SPONSORED BY  :  IPT - INTELLECTUAL PRODUCTS & TECHNOLOGIES Ltd.
 * PROJECT       :  IPT Hackathon :: Cocktail Machine
 * FILENAME      :  CoctailMachine.java  
 * 
 * This file is part of the ROBOLEARN project. More information about 
 * this project can be found here:  http://robolearn.org/
 * ***********************************************************************
 * 
 * Copyright (C) 2015 IPT Hackathon :: Cocktail Machine project participants.
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
 * In case you modify this file,
 * please add the appropriate notice below the existing Copyright notices,
 * with the fields enclosed in brackets {} replaced by your own identification:
 * "Portions Copyright (c) {year} {name of copyright owner}"
 */

package cocktails;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

public class CoctailMachine {

	private static final int TARGET_IMPULS = 15;

	public static void flowMeter(List<PumpRunner> pumps) {

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pin as an input pin with its internal pull down
		// resistor enabled
		for (PumpRunner pumpRunner : pumps) {
			final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(
					pumpRunner.getPin(), PinPullResistance.PULL_DOWN);
			myButton.addListener(new PumpListener(pumpRunner));
		}

	}

	public static void main(String[] args) throws InterruptedException {
		List<PumpRunner> pumps = new ArrayList<PumpRunner>();
		pumps.add(new PumpRunner(17, TARGET_IMPULS, RaspiPin.GPIO_04));
		pumps.add(new PumpRunner(27, TARGET_IMPULS, RaspiPin.GPIO_05));
		pumps.add(new PumpRunner(22, TARGET_IMPULS, RaspiPin.GPIO_06));

		flowMeter(pumps);

		List<Thread> threads = new ArrayList<Thread>();
		int i = 0;
		for (PumpRunner pumpRunner : pumps) {
			Thread thread = new Thread(pumpRunner, "Pump " + i++);
			thread.start();

			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.join();
		}

		// runAllPumps();
		System.out.println("End of the demo.");
	}

	static class PumpRunner implements Runnable {

		private volatile boolean finish = false;
		private int pin;
		private Pin pinIn;
		private int targetCount;

		public PumpRunner(int pin, int targetCount, Pin pinIn) {
			this.pin = pin;
			this.pinIn = pinIn;
			this.targetCount = targetCount;
		}

		public Pin getPin() {
			return pinIn;
		}

		public int getTargetCount() {
			return targetCount;
		}

		public void stop() {
			finish = true;
		}

		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName()
					+ " started on PIN " + pin);
			Gpio.wiringPiSetupGpio();
			Gpio.pinMode(pin, Gpio.OUTPUT);
			Gpio.pullUpDnControl(pin, Gpio.PUD_DOWN);
			Gpio.digitalWrite(pin, true);

			while (!finish) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					stop();
				}
			}

			Gpio.digitalWrite(pin, false);
			System.out.println(Thread.currentThread().getName() + " finished.");
		}

	}

	static class PumpListener implements GpioPinListenerDigital {

		private int counter = 0;
		private int targetCount;
		private PumpRunner pump;

		public PumpListener(PumpRunner pump) {
			this.targetCount = pump.getTargetCount();
			this.pump = pump;
		}

		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {
			counter++;
			System.out.println("--> GPIO PIN STATE CHANGE " + event.getPin()
					+ " " + event.getState() + " --> " + counter);
			if (counter >= targetCount) {
				pump.stop();
			}
		}

		public int getCounter() {
			return counter;
		}
	}
}
