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
import static motors_demo.Direction.*;

public class LineFollower01 {
	private final EV3 ev3 = (EV3) BrickFinder.getLocal();
	private TextLCD lcd = ev3.getTextLCD();
	private Keys keys = ev3.getKeys();
	private EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
	private EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
	RegulatedMotor mA, mB, mC;
	SensorMode color;
	float[] colorSample;
	SensorMode touch;
	float[] sample;

	public LineFollower01() {
		mA = new EV3LargeRegulatedMotor(MotorPort.A);
		mB = new EV3LargeRegulatedMotor(MotorPort.B);
		mC = new EV3LargeRegulatedMotor(MotorPort.C);
		mB.setSpeed(120);// 2 RPM
		mC.setSpeed(120);
		mB.synchronizeWith(new RegulatedMotor[] { mC });
		color = colorSensor.getRGBMode();
		colorSample = new float[color.sampleSize()];
		touch = touchSensor.getTouchMode();
		sample = new float[touch.sampleSize()];
	}

	public void start() {
		// Audio audio = ev3.getAudio();

		// Color sensor

		// playMessage(audio);

		long startTime = System.currentTimeMillis();
		long duration;
		int lastColorId = Color.NONE;

		// for (int i = 0; i < 4; i++) {
		do {
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
			} while (duration < 120000 && mB.isMoving() && mC.isMoving() && sample[0] == 0 && isReflecting(colorSample)
					&& isRed(colorSample));
			mB.startSynchronization();
			mB.stop();
			mC.stop();
			mB.endSynchronization();

			// Seek the red line
			seekRed();
		} while (duration < 120000 && mB.isMoving() && mC.isMoving() && sample[0] == 0 && isReflecting(colorSample));
		// // go back
		// mB.startSynchronization();
		// mB.backward();
		// mC.backward();
		// mB.endSynchronization();
		//
		// // turn back
		// mB.startSynchronization();
		// mB.rotate(1000, true);
		// mC.rotate(-1000, true);
		// mB.endSynchronization();
		// while (mB.isMoving() && mC.isMoving())
		// Thread.yield();

		// }

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

		// getColor(lcd, color, colorSample);
		// keys.waitForAnyPress();

	}

	public static void main(String[] args) {
		LineFollower01 lineFollower = new LineFollower01();
		lineFollower.start();
	}

	private void seekRed() {
		long treshold = 500;
		Direction direction = Direction.LEFT;
		long seekStratTime = System.currentTimeMillis();
		long seekDuration;
		do {
			long startRotationTime = System.currentTimeMillis();
			treshold *= 1.2;
			direction = (direction == LEFT) ? RIGHT : LEFT;
			long duration;
			do {
				// turn
				mB.startSynchronization();
				if (direction == LEFT) {
					mB.forward();
					mC.backward();
				} else {
					mB.backward();
					mC.forward();
				}
				mB.endSynchronization();
				duration = System.currentTimeMillis() - startRotationTime;
				color.fetchSample(colorSample, 0);
				touch.fetchSample(sample, 0);
			} while (duration < treshold && !isRed(colorSample));
			seekDuration = seekStratTime - System.currentTimeMillis();
		} while (seekDuration < 10000 && mB.isMoving() && mC.isMoving() && sample[0] == 0 && isReflecting(colorSample)
				&& !isRed(colorSample));
	}

	private void playMessage(final Audio audio) {
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

	private boolean isRed(float[] colorSample) {
		return colorSample[0] > 0.12 && colorSample[1] < 0.04 && colorSample[2] < 0.04;
	}

	private boolean isReflecting(float[] colorSample) {
		return colorSample[0] > 0.015 || colorSample[1] > 0.015 || colorSample[2] > 0.015;
	}

	// private static boolean findRedTrack()

}
