package org.iproduct.iptpi.domain.audio;

public class AudioFactory {
	private static AudioPlayer theInstance;
	public static AudioPlayer createAudioPlayer() {
		if(theInstance == null)
			theInstance = new AudioPlayer();
		return theInstance;
	}
} 
