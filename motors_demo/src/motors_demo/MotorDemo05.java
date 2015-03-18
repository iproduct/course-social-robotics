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
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MotorDemo05 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
		SensorMode touch = touchSensor.getTouchMode();
		float[] sample = new float[touch.sampleSize()];

		RegulatedMotor mA, mB, mC;
		// mA.resetTachoCount();
		// mB.resetTachoCount();
		// mC.resetTachoCount();

		// mA.rotateTo(760);
		// int angle = mA.getTachoCount(); // should be -360
		// lcd.drawInt(angle, 0, 0);
		// keys.waitForAnyPress();

		long startTime = System.currentTimeMillis();
		long duration;

		for (int i = 0; i < 3; i++) {
			mA = new EV3LargeRegulatedMotor(MotorPort.A);
			mB = new EV3LargeRegulatedMotor(MotorPort.B);
			mC = new EV3LargeRegulatedMotor(MotorPort.C);
			mB.setSpeed(360);// 2 RPM
			mC.setSpeed(360);
			mB.synchronizeWith(new RegulatedMotor[] { mC });

			// go forward
			mB.startSynchronization();
			mB.forward();
			mC.forward();
			mB.endSynchronization();

			// go until not obstacle
			do {
				duration = System.currentTimeMillis() - startTime;
				touch.fetchSample(sample, 0);
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

			mB.flt();
			mC.flt();

			mA.close();
			mB.close();
			mC.close();

			Audio audio = ev3.getAudio();
			lcd.drawInt(audio.playSample(new File("az_robot.wav"), 50), 0, 1);
			keys.waitForAnyEvent();
//			Delay.msDelay(3000);
			
		}

		// mA.close();
		// mB.close();
		// mC.close();

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
	}

}
