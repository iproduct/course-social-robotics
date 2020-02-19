package hello;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MoveRobot {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		// lcd.drawString("Hello Trayan", 4, 2);
		// lcd.drawString("from leJOS", 4, 3);

		// Move forward
		RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
		mB.synchronizeWith(new RegulatedMotor[] { mC });

		mB.startSynchronization();
		mB.forward();
		mC.forward();
		mB.endSynchronization();

		Delay.msDelay(3000);
		mB.startSynchronization();
		mB.stop();
		mC.stop();
		mB.endSynchronization();

		// keys.waitForAnyPress(5000);
	}
}
