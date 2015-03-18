

import javax.print.attribute.standard.PrinterIsAcceptingJobs;

import lejos.hardware.Audio;
import static lejos.hardware.Audio.*;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class AudioDemo01 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		Audio audio = ev3.getAudio();
		audio.setVolume(10);
		audio.playNote(Audio.XYLOPHONE, 420, 500);
		audio.playNote(Audio.XYLOPHONE, 510, 500);
		audio.playNote(Audio.XYLOPHONE, 420, 500);
		audio.playNote(Audio.XYLOPHONE, 640, 500);
		audio.playNote(Audio.XYLOPHONE, 220, 500);
		
		
	}

}
