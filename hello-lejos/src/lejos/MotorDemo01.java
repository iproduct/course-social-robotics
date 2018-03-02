package lejos;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MotorDemo01 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
		mA.resetTachoCount();
		
		// Catch bottle
		for (int i = 0; i < 1; i++) {
			mA.rotateTo(-500);
			mA.rotateTo(500);
		}
		
		mB.synchronizeWith(new RegulatedMotor[] { mC });
		
		// Move forward
		mB.setSpeed(720);// 2 RPM
		mC.setSpeed(720);
		mB.startSynchronization();
		mB.forward();
		mC.forward();
		mB.endSynchronization();
		Delay.msDelay(1000);
		mB.startSynchronization();
		mB.flt();
		mC.flt();
		mB.endSynchronization();
		
		// Drop bottele
		mA.rotateTo(-500);	
		
		// Close
		mA.close();
		mB.close();
		mC.close();
	}

}
