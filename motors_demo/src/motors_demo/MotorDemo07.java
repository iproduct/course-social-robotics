package motors_demo;

import java.io.File;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MotorDemo07 {

	public static void main(String[] args) {
		final EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		Audio audio = ev3.getAudio();
		
		//Color sensor
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
		SensorMode color = colorSensor.getColorIDMode();
		float[] colorSample = new float[color.sampleSize()];
		
		playMessage(audio);

		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
		SensorMode touch = touchSensor.getTouchMode();
		float[] sample = new float[touch.sampleSize()];

		RegulatedMotor mA, mB, mC;
		mA = new EV3LargeRegulatedMotor(MotorPort.A);
		mB = new EV3LargeRegulatedMotor(MotorPort.B);
		mC = new EV3LargeRegulatedMotor(MotorPort.C);
		mB.setSpeed(720);// 2 RPM
		mC.setSpeed(720);
		mB.synchronizeWith(new RegulatedMotor[] { mC });
		
		long startTime = System.currentTimeMillis();
		long duration;
		int lastColorId = Color.NONE;

		for (int i = 0; i < 4; i++) {

			// go forward
			mB.startSynchronization();
			mB.forward();
			mC.forward();
			mB.endSynchronization();

			// go until not obstacle
			do {
				duration = System.currentTimeMillis() - startTime;
				touch.fetchSample(sample, 0);
				color.fetchSample(colorSample, 0);
				int colorId = (int)colorSample[0];
				String colorName = "";
				switch(colorId){
					case Color.NONE: colorName = "NONE"; break;
					case Color.BLACK: colorName = "BLACK"; break;
					case Color.BLUE: colorName = "BLUE"; break;
					case Color.GREEN: colorName = "GREEN"; break;
					case Color.YELLOW: colorName = "YELLOW"; break;
					case Color.RED: colorName = "RED"; break;
					case Color.WHITE: colorName = "WHITE"; break;
					case Color.BROWN: colorName = "BROWN"; break;
				}
				lcd.drawString(colorId + " - " + colorName, 0, 0);
			} while (duration < 60000 && mB.isMoving() && mC.isMoving()
					&& sample[0] == 0);

			// go back
			mB.startSynchronization();
			mB.backward();
			mC.backward();
			mB.endSynchronization();
			Delay.msDelay(700);

			// turn back
			mB.startSynchronization();
			mB.rotate(1000, true);
			mC.rotate(-1000, true);
			mB.endSynchronization();
			while (mB.isMoving() && mC.isMoving())
				Thread.yield();

			playMessage(audio);

			
		}
		mB.flt();
		mC.flt();

		mA.close();
		mB.close();
		mC.close();

		// Delay.msDelay(1000);
		// mB.stop();
		// mC.stop();
		// mB.rotateTo(360);
		// mB.rotate(-720, true);
		// while (mB.isMoving())
		// Thread.yield();
		// angle = mB.getTachoCount();
		// lcd.drawInt(angle, 0, 1);
		//
		// mA.rotateTo(-100, true);
		// do{
		// touch.fetchSample(sample, 0);
		// } while (mA.isMoving() && sample[0] == 0);
		// mA.stop();
		//
		// angle = mA.getTachoCount(); // should be -360
		// lcd.drawInt(angle, 0, 2);
		// lcd.drawInt((int)duration, 0, 1);
		// keys.waitForAnyEvent();
		
		keys.waitForAnyPress();
	}

	private static void playMessage(final Audio audio) {
		Thread t = new Thread() {
			@Override
			public void run() {
				audio.playSample(new File("pozdrav_01.wav"), 100);
			}
		};
		t.setDaemon(true);
		t.start();
		Delay.msDelay(6000);
		t.interrupt();
	}

}
