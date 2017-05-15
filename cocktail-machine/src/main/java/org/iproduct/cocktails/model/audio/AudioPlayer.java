package org.iproduct.cocktails.model.audio;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import com.pi4j.wiringpi.Gpio;


public class AudioPlayer {

	public AudioPlayer() {
		// initialize wiringPi library
		// Gpio.wiringPiSetupGpio();

		// GPIO output pin
		Gpio.pinMode(17, Gpio.OUTPUT);
		Gpio.pullUpDnControl(17, Gpio.PUD_DOWN);
	}

	public void enable() {
		Gpio.digitalWrite(17, true);
	}

	public void disable() {
		Gpio.digitalWrite(17, false);
	}

	public void play() {

//		String strFilename = "/home/pi/Music/11. Ancient Voices.mp3";
//		String strFilename = "/home/pi/Music/03. For the Joy of It All.mp3";
//		String strFilename = "/home/pi/Music/07. Sunrise at the Ganges.mp3";
//		String strFilename = "/home/pi/az_robot.wav";
		String strFilename = "/home/pi/dev-bg.mp3";
		File soundFile = new File(strFilename);

		new Thread(() -> {
			System.out.println(soundFile);
			enable(); // enable speakers power
			try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile)) {
				// AudioFormat audioFormat = audioInputStream.getFormat();
				// DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				// audioFormat, BUFFER_SIZE);
				Mixer.Info[] mixers = AudioSystem.getMixerInfo();
				for (int i = 0; i < mixers.length; i++) {
					System.out.println("MIXERS " + i + " : " + mixers[i]);
				}
				play(strFilename, mixers[3]);
				
				
//				try (Clip clip = AudioSystem.getClip(mixers[3])) {
//					System.out.println("MIXER CHOSEN: " + mixers[3]);
//					clip.open(audioInputStream);
//					clip.setFramePosition(0);
//					System.out.println(Arrays.toString(clip.getControls()));
//					clip.start();
//					clip.drain();
//
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//					clip.stop();
//					clip.flush();
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					clip.close();
//
//				} catch (LineUnavailableException e) {
//					e.printStackTrace();
//					// System.exit(1);
//				}

			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			disable(); // disable speakers power
			System.out.println("Audio playback finished.");

		}).start();

	}
	
	 public void play(String filePath, Mixer.Info mixerInfo) {
	        final File file = new File(filePath);
	 
	        try (final AudioInputStream in = AudioSystem.getAudioInputStream(file)) {
	             
	            final AudioFormat outFormat = getOutFormat(in.getFormat());
	            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);
	 
	            try (final SourceDataLine line =
	                     (SourceDataLine) AudioSystem.getSourceDataLine(outFormat, mixerInfo)) {
	 
	                if (line != null) {
	                    line.open(outFormat);
	                    line.start();
	                    stream(AudioSystem.getAudioInputStream(outFormat, in), line);
	                    line.drain();
	                    line.stop();
	                }
	            }
	 
	        } catch (UnsupportedAudioFileException 
	               | LineUnavailableException 
	               | IOException e) {
	            throw new IllegalStateException(e);
	        }
	    }
	 
	    private AudioFormat getOutFormat(AudioFormat inFormat) {
	        final int ch = inFormat.getChannels();
	        final float rate = inFormat.getSampleRate();
	        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	    }
	 
	    private void stream(AudioInputStream in, SourceDataLine line) 
	        throws IOException {
	        final byte[] buffer = new byte[65536];
	        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
	            line.write(buffer, 0, n);
	        }
	    }

}