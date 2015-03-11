package motors_demo;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class MotorDemo02 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		Motor.B.setSpeed(720);// 2 RPM
		Motor.C.setSpeed(720);
		Motor.B.forward();
		Motor.C.forward();
		Delay.msDelay(1000);
		Motor.B.stop();
		Motor.C.stop();
		Motor.B.rotateTo(360);
		Motor.B.rotate(-720, true);
		while (Motor.B.isMoving())
			Thread.yield();
		int angle = Motor.B.getTachoCount(); // should be -360
		LCD.drawInt(angle, 0, 0);
		keys.waitForAnyEvent();
		
	}

}
