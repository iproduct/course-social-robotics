
import java.io.File;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;

public class AudioDemo02 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		// File root = new File(".");
		// String[] files = root.list();
		// for(int i = 0; i < files.length; i++)
		// lcd.drawString(files[i], 0, i);

		Audio audio = ev3.getAudio();
		audio.playSample(new File("hackathon.wav"), 100);

		LCD.drawString("Enter to Exit", 0, 2);
		Button.ENTER.waitForPressAndRelease();

		// audio.playNote(Audio.XYLOPHONE, 420, 500);
		// audio.playNote(Audio.XYLOPHONE, 510, 500);
		// audio.playNote(Audio.XYLOPHONE, 420, 500);
		// audio.playNote(Audio.XYLOPHONE, 640, 500);
		// audio.playNote(Audio.XYLOPHONE, 220, 500);
		// keys.waitForAnyPress();

	}

}
