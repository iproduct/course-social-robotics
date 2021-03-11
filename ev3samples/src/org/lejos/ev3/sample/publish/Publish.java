package org.lejos.ev3.sample.publish;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;
import lejos.utility.Delay;

/**
 * This sample uses the PublishFilter in lejos.robotics.filter to publish sensor readings.
 * 
 * It assumes that there is an IR sensor attached to sensor port 1, an ultrasonic sensor
 * attached to port 2, and a Gyro sensor attached to port 3.
 * 
 * Another program, which would normally be running on a PC, can then subscribe to the topics to see the data.
 * 
 * If you run the PublishedSamples PC sample program, you can see a list of the available topics to subscribe to.
 * 
 * @author Lawrie Griffiths
 *
 */
public class Publish {
	
	public static void introMessage() {
		
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Publish", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		 
		g.drawString("This sample demonstrates how ", 2, 20, 0);
		g.drawString("to publish data using the  ", 2, 30, 0);
		g.drawString("PublishFilter. It needs ", 2, 40, 0);
		g.drawString("an EV3 IR sensor in sensor ", 2, 50, 0);
		g.drawString("port 1 , an ultrasonic sensor", 2, 60, 0);
		g.drawString("in port 2 and a Gyro sensor ", 2, 70, 0);
		g.drawString("attached t0 port 3.", 2, 80, 0); 
		  
		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit/2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit+7, 0);
		g.drawLine(0, y_quit,  45, y_quit); // top line
		g.drawLine(0, y_quit,  0, y_quit+height_quit-arc_diam/2); // left line
		g.drawLine(width_quit, y_quit,  width_quit, y_quit+height_quit/2); // right line
		g.drawLine(0+arc_diam/2, y_quit+height_quit,  width_quit-10, y_quit+height_quit); // bottom line
		g.drawLine(width_quit-10, y_quit+height_quit, width_quit, y_quit+height_quit/2); // diagonal
		g.drawArc(0, y_quit+height_quit-arc_diam, arc_diam, arc_diam, 180, 90);
		
		// Enter GUI button:
		g.fillRect(width_quit+10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit+15, y_quit+7, 0,true);
		
		Button.waitForAnyPress();
		if(Button.ESCAPE.isDown()) System.exit(0);
		g.clear();
	}
	public static void main(String[] args) throws IOException {
		introMessage();
		float frequency = 1; // 1 sample per second
		EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S1);
		SampleProvider sp = new PublishFilter(irSensor.getDistanceMode(), "IR range readings", frequency);
		float[] sample = new float[sp.sampleSize()];
		
		EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		SampleProvider sp2 = new PublishFilter(sonicSensor.getDistanceMode(), "Ultrasonic readings", frequency);
		float[] sample2 = new float[sp2.sampleSize()];
		
		EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S3);
		SampleProvider sp3 = new PublishFilter(gyroSensor.getRateMode(), "Gyro readings", frequency);
		float[] sample3 = new float[sp3.sampleSize()];
		
		while(Button.ESCAPE.isUp()) {
			sp.fetchSample(sample, 0);
			LCD.clear(3);
			LCD.drawString("IR: " + sample[0],0,3);
			sp2.fetchSample(sample2, 0);
			LCD.clear(4);
			LCD.drawString("Sonic: " + sample2[0],0,4);
			sp3.fetchSample(sample3, 0);
			LCD.clear(5);
			LCD.drawString("Gyro: " + sample3[0],0,5);
			Delay.msDelay((long) (1000/frequency));
		}
		
		irSensor.close();
		sonicSensor.close();
		gyroSensor.close();
	}
}
