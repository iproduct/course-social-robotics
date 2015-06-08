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

public class LineFollower01 {

	public static void main(String[] args) {
		final EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
//		Audio audio = ev3.getAudio();
		
		//Color sensor
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
		SensorMode color = colorSensor.getRGBMode();
		float[] colorSample = new float[color.sampleSize()];
		
//		playMessage(audio);

		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
		SensorMode touch = touchSensor.getTouchMode();
		float[] sample = new float[touch.sampleSize()];

		RegulatedMotor mA, mB, mC;
		mA = new EV3LargeRegulatedMotor(MotorPort.A);
		mB = new EV3LargeRegulatedMotor(MotorPort.B);
		mC = new EV3LargeRegulatedMotor(MotorPort.C);
		mB.setSpeed(120);// 2 RPM
		mC.setSpeed(120);
		mB.synchronizeWith(new RegulatedMotor[] { mC });
		
		long startTime = System.currentTimeMillis();
		long duration;
		int lastColorId = Color.NONE;

//		for (int i = 0; i < 4; i++) {

			// go forward
			mB.startSynchronization();
			mB.forward();
			mC.forward();
			mB.endSynchronization();

			// go forward while on red track and no obstacle
			do {
				duration = System.currentTimeMillis() - startTime;
				touch.fetchSample(sample, 0);
				color.fetchSample(colorSample, 0);
				lcd.drawString("" + colorSample[0], 0, 3);
				lcd.drawString("" + colorSample[1], 0, 4);
				lcd.drawString("" + colorSample[2], 0, 5);
			} while (duration < 60000 && mB.isMoving() && mC.isMoving()
					&& sample[0] == 0 && isReflecting(colorSample)
					&& isRed(colorSample));
			mB.startSynchronization();
			mB.stop();
			mC.stop();
			mB.endSynchronization();
			
			//Seek the red line 
//			seekRed();

//			// go back
//			mB.startSynchronization();
//			mB.backward();
//			mC.backward();
//			mB.endSynchronization();
//
//			// turn back
//			mB.startSynchronization();
//			mB.rotate(1000, true);
//			mC.rotate(-1000, true);
//			mB.endSynchronization();
//			while (mB.isMoving() && mC.isMoving())
//				Thread.yield();

//		}

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
		
//		getColor(lcd, color, colorSample);
//		keys.waitForAnyPress();
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
	
	private static boolean isRed(float[] colorSample){
		return colorSample[0] > 0.05 
				&& colorSample[1] < 0.04 
				&& colorSample[2] < 0.04;
	}

	private static boolean isReflecting(float[] colorSample){
		return colorSample[0] > 0.015 
				|| colorSample[1] > 0.015 
				|| colorSample[2] > 0.015;
	}
	
//	private static boolean findRedTrack()

}
