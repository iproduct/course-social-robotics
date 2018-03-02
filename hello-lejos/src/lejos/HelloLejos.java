package lejos;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class HelloLejos {
	public static void main(String[] args) {
		LCD.drawString("Hello, Trayan!", 0, 4);
		Delay.msDelay(5000);
	}

}
