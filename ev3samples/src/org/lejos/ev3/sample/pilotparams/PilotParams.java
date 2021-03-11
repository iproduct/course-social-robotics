package org.lejos.ev3.sample.pilotparams;
import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.PilotProps;

/**
 * Use this sample to set the parameters for DifferentialPilot. These values
 * will then work with any sample that uses DifferentialPilot.
 * 
 * @author Lawrie Griffiths
 *
 */
public class PilotParams {

	public static void main(String[] args) throws IOException {
		introMessage();
		
		// Change this to match your robot
		PilotProps p = new PilotProps();
		p.setProperty(PilotProps.KEY_WHEELDIAMETER, "3.5");
		p.setProperty(PilotProps.KEY_TRACKWIDTH, "20.0");
		p.setProperty(PilotProps.KEY_LEFTMOTOR, "B");
		p.setProperty(PilotProps.KEY_RIGHTMOTOR, "C");
		p.setProperty(PilotProps.KEY_REVERSE, "false");
		
		p.storePersistentValues();
	}
	
	public static void introMessage() {
		
		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Pilot Settings", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		 
		 
		g.drawString("Use this tool to set the     ", 2, 20, 0);
		g.drawString("parameters for", 2, 30, 0);
		g.drawString("DifferentialPilot. These", 2, 40, 0);
		g.drawString("values will then work with", 2, 50, 0);
		g.drawString("any sample that uses", 2, 60, 0);
		g.drawString("DifferentialPilot, such as ", 2, 70, 0); 
		g.drawString("the PilotTester sample.", 2, 80, 0);
		g.drawString("Note:Must change source code!", 2, 90, 0);
		
		  
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
    
}
