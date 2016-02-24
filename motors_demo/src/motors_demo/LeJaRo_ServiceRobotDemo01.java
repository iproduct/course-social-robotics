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

public class LeJaRo_ServiceRobotDemo01 {
	public static final long MAX_DURATION = 60000; 
	final EV3 ev3 = (EV3) BrickFinder.getLocal();
	RegulatedMotor 
			mA = new EV3LargeRegulatedMotor(MotorPort.A), 
			mB = new EV3LargeRegulatedMotor(MotorPort.B),
			mC = new EV3LargeRegulatedMotor(MotorPort.C);
	TextLCD lcd = ev3.getTextLCD();
	Keys keys = ev3.getKeys();
	// Audio audio = ev3.getAudio();

	// Color sensor
	EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
	SensorMode color = colorSensor.getRGBMode();
	float[] colorSample = new float[color.sampleSize()];

	// Touch sensor
	EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
	SensorMode touch = touchSensor.getTouchMode();
	float[] sample = new float[touch.sampleSize()];

	public LeJaRo_ServiceRobotDemo01() {
		mB.setSpeed(150);// degrees per second
		mC.setSpeed(150);
		mB.synchronizeWith(new RegulatedMotor[] { mC });
	}

	public void init() {
			//reset the hand to initial position
			lcd.drawString("Initial positioning", 0, 0);
			mA.rotateTo(-700); //hand fully open
			mA.resetTachoCount();
			mA.rotate(350); //optical sensor horizontal in front
		}

	public void goForward() {
		// go forward
		mB.startSynchronization();
		mB.forward();
		mC.forward();
		mB.endSynchronization();
	}
	
	public void goBackwards() {
		mB.startSynchronization();
		mB.backward();
		mC.backward();
		mB.endSynchronization();
	}

	public void goForwardDegrees(int degrees) {
		mB.startSynchronization();
		mB.rotate(degrees, true);
		mC.rotate(degrees, true);
		mB.endSynchronization();
		while (mB.isMoving() && mC.isMoving())
			Thread.yield();
	}
	
	public void goBackwardsDegrees(int degrees) {
		mB.startSynchronization();
		mB.rotate(-degrees, true);
		mC.rotate(-degrees, true);
		mB.endSynchronization();
		while (mB.isMoving() && mC.isMoving())
			Thread.yield();
	}

	public void moveUntilEvent() {
		moveUntilEvent(MAX_DURATION);
	}

	public void moveUntilEvent(long maxDuration) {
		long startTime = System.currentTimeMillis();
		long duration;

		// go until not obstacle
		do {
			duration = System.currentTimeMillis() - startTime;
			touch.fetchSample(sample, 0);
			color.fetchSample(colorSample, 0);
			lcd.drawString("" + colorSample[0], 0, 3);
			lcd.drawString("" + colorSample[1], 0, 4);
			lcd.drawString("" + colorSample[2], 0, 5);
		} while (duration < maxDuration && mB.isMoving() && mC.isMoving() 
				&& sample[0] == 0 && isReflecting()
				&& !isOnWhite());
	}

	public void turn180Degree() {
		mB.startSynchronization();
		mB.rotate(1000, true);
		mC.rotate(-1000, true);
		mB.endSynchronization();
		while (mB.isMoving() && mC.isMoving())
			Thread.yield();
	}

	public void graspTheCup() {
		mA.rotateTo(0); // hand fully open

		mB.rotate(600, true); // go slightly forward
		mC.rotate(600);

		mA.rotate(760); // grasp the cup
	}
	
	public void leaveTheCup() {
		mA.rotateTo(0); // hand fully open

		mB.rotate(-400, true); // go slightly forward
		mC.rotate(-400);

		mA.rotate(760); // close hand
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
	
	public void finishDemo() {
		//stop motors
		mB.flt();
		mC.flt();

		// finish
		mA.close();
		mB.close();
		mC.close();
	}

	// Predicates
	private boolean isReflecting() {
		return colorSample[0] > 0.015 || colorSample[1] > 0.015 || colorSample[2] > 0.015;
	}

	private boolean isOnWhite() {
		return colorSample[0] > 0.07 && colorSample[1] > 0.07 && colorSample[2] > 0.07;
	}

	
	public static void main(String[] args) {

		LeJaRo_ServiceRobotDemo01 lejaro = new LeJaRo_ServiceRobotDemo01();
		lejaro.init();
		
		for (int i = 0; i < 4; i++) {
			lejaro.goForward();
			lejaro.moveUntilEvent();
			if (lejaro.isOnWhite()) { // grasp the cup
				lejaro.graspTheCup();
				lejaro.goBackwardsDegrees(300);
				lejaro.turn180Degree();
				lejaro.goForwardDegrees(1000);
				lejaro.leaveTheCup();
				break; // finish demo
			}

			lejaro.goBackwardsDegrees(300);

			// turn back
			lejaro.turn180Degree();

		}
		lejaro.finishDemo();
	}
}
