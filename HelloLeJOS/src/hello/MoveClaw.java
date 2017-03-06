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

public class MoveClaw {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		// lcd.drawString("Hello Trayan", 4, 2);
		// lcd.drawString("from leJOS", 4, 3);

		// Open clow
		RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);

		while (true) {
			mA.resetTachoCount();
			lcd.drawString("Up - Close", 4, 2);
			lcd.drawString("Down - Open", 4, 3);
			lcd.drawString("Escape - Exit", 4, 4);

			// Simple menu
			keys.waitForAnyPress(60000);
			int maRotation = 0;
			if (Button.DOWN.isDown()) {
				maRotation = -620;
			} else if (Button.UP.isDown()) {
				maRotation = 620;
			}
			if (Button.ESCAPE.isDown()) {
				System.exit(0);
			}

			mA.rotateTo(maRotation, false);
			int count = mA.getTachoCount();
			lcd.drawString("Tacho: " + count, 0, 2);
		}

	}
}
