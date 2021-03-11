package org.lejos.ev3.sample.followbeacon;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

/**
 * Requires a wheeled vehicle with two independently controlled
 * motors connected to motor ports B and C, and 
 * an EV3 IR sensor connected to port 4.
 * 
 * @author Lawrie Griffiths
 */
public class Follow {
	
	  public static void introMessage() {

		  GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		  g.drawString("Follow Beacon Demo", 5, 0, 0);
		  g.setFont(Font.getSmallFont());
		  g.drawString("Demonstration of IR Beacon", 2, 20, 0);
		  g.drawString("seek mode. Requires", 2, 30, 0);
		  g.drawString("a wheeled vehicle with two", 2, 40, 0);
		  g.drawString("independently controlled", 2, 50, 0);
		  g.drawString("motors connected to motor", 2, 60, 0);
		  g.drawString("ports B and C, and an", 2, 70, 0); 
		  g.drawString("infrared sensor connected", 2, 80, 0);
		  g.drawString("to port 4.", 2, 90, 0);
		  
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

	public static void main(String[] args) {
		
		introMessage();
		
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S3);
		RegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.C);
		SensorMode seek = ir.getSeekMode();
		float[] sample = new float[seek.sampleSize()];
		
		while(Button.ESCAPE.isUp()) {
			seek.fetchSample(sample, 0);
			int direction = (int) sample[0];
			System.out.println("Direction: " + direction);
			int distance = (int) sample[1];
			
			if (direction > 0) {
				left.forward();
				right.stop(true);
			} else if (direction < 0) {
				right.forward();
				left.stop(true);
			} else {
				if (distance < Integer.MAX_VALUE) {
					left.forward();
					right.forward();
				} else {
					left.stop(true);
					right.stop(true);
				}
			}
		}
		
		left.close();
		right.close();
		ir.close();
	}
}
