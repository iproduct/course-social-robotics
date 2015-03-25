package motors_demo;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class MotorDemo01 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		RegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.A);
		m.resetTachoCount();
		
		m.rotateTo(-40);
		int angle = m.getTachoCount(); // should be 760
		lcd.drawInt(angle, 0, 0);
//		keys.waitForAnyPress();
	
//		m.rotateTo(0);
//		angle = m.getTachoCount(); // should be 0
//		lcd.drawInt(angle, 0, 1);
//		keys.waitForAnyPress(); // wait for a button press
		
		m.close();
	}

}
