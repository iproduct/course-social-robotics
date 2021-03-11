package org.lejos.ev3.sample.bumpercar;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Demonstration of the Behavior subsumption classes.
 * 
 * Requires a wheeled vehicle with two independently controlled
 * motors connected to motor ports B and C, and 
 * an EV3 IR sensor connected to port 4;
 * 
 * @author Brian Bagnall and Lawrie Griffiths, modified by Roger Glassey
 *
 */
public class EV3BumperCar
{
    //static RegulatedMotor leftMotor = MirrorMotor.invertMotor(Motor.A);
    //static RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.B);
    static RegulatedMotor leftMotor = Motor.B;
    static RegulatedMotor rightMotor = Motor.C;
    static IRSensor sensor;
  
  // Use these definitions instead if your motors are inverted
  // static RegulatedMotor leftMotor = MirrorMotor.invertMotor(Motor.A);
  //static RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.C);
  
  public static void introMessage() {

		  GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		  g.drawString("Bumper Car Demo", 5, 0, 0);
		  g.setFont(Font.getSmallFont());
		  g.drawString("Demonstration of the Behavior", 2, 20, 0);
		  g.drawString("subsumption classes. Requires", 2, 30, 0);
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
    
  public static void main(String[] args)
  {
	  introMessage();
	  
      leftMotor.resetTachoCount();
      rightMotor.resetTachoCount();
    leftMotor.rotateTo(0);
    rightMotor.rotateTo(0);
    leftMotor.setSpeed(400);
    rightMotor.setSpeed(400);
    leftMotor.setAcceleration(800);
    rightMotor.setAcceleration(800);
    sensor = new IRSensor();
    sensor.setDaemon(true);
    sensor.start();
    Behavior b1 = new DriveForward();
    Behavior b2 = new DetectWall();
    Behavior[] behaviorList =
    {
      b1, b2
    };
    Arbitrator arbitrator = new Arbitrator(behaviorList);
    LCD.drawString("Bumper Car",0,1);
    Button.LEDPattern(6);
    Button.waitForAnyPress();
    arbitrator.go();
  }
}

class IRSensor extends Thread
{
    EV3IRSensor ir = new EV3IRSensor(SensorPort.S3);
    SampleProvider sp = ir.getDistanceMode();
    public int control = 0;
    public int distance = 255;

    IRSensor()
    {

    }
    
    public void run()
    {
        while (true)
        {
            float [] sample = new float[sp.sampleSize()];
            control = ir.getRemoteCommand(0);
            sp.fetchSample(sample, 0);
            distance = (int)sample[0];
            System.out.println("Control: " + control + " Distance: " + distance);
            
        }
        
    }
}

class DriveForward implements Behavior
{

  private boolean _suppressed = false;

  public boolean takeControl()
  {
      if (Button.readButtons() != 0)
      {
          _suppressed = true;
          EV3BumperCar.leftMotor.stop();
          EV3BumperCar.rightMotor.stop();          
          Button.LEDPattern(6);
          Button.discardEvents();
          System.out.println("Button pressed");
          if ((Button.waitForAnyPress() & Button.ID_ESCAPE) != 0)
          {
              Button.LEDPattern(0);
              System.exit(1);
          }
          System.out.println("Button pressed 2");
          Button.waitForAnyEvent();
          System.out.println("Button released");
      }
    return true;  // this behavior always wants control.
  }

  public void suppress()
  {
    _suppressed = true;// standard practice for suppress methods
  }

  public void action()
  {
    _suppressed = false;
    //EV3BumperCar.leftMotor.forward();
    //EV3BumperCar.rightMotor.forward();
    while (!_suppressed)
    {
      //EV3BumperCar.leftMotor.forward();
      //EV3BumperCar.rightMotor.forward();
        switch(EV3BumperCar.sensor.control)
        {
        case 0:
            EV3BumperCar.leftMotor.setSpeed(400);
            EV3BumperCar.rightMotor.setSpeed(400);
            EV3BumperCar.leftMotor.stop(true);
            EV3BumperCar.rightMotor.stop(true);
            break;
        case 1:
            EV3BumperCar.leftMotor.setSpeed(400);
            EV3BumperCar.rightMotor.setSpeed(400);
            EV3BumperCar.leftMotor.forward();
            EV3BumperCar.rightMotor.forward();
            break;
        case 2:
            EV3BumperCar.leftMotor.backward();
            EV3BumperCar.rightMotor.backward();
            break;
        case 3:
            EV3BumperCar.leftMotor.setSpeed(200);
            EV3BumperCar.rightMotor.setSpeed(200);
            EV3BumperCar.leftMotor.forward();
            EV3BumperCar.rightMotor.backward();
            break;
        case 4:
            EV3BumperCar.leftMotor.setSpeed(200);
            EV3BumperCar.rightMotor.setSpeed(200);
            EV3BumperCar.leftMotor.backward();
            EV3BumperCar.rightMotor.forward();
            break;

            
        }
      Thread.yield(); //don't exit till suppressed
    }
    //EV3BumperCar.leftMotor.stop(true); 
    //EV3BumperCar.rightMotor.stop(true);
    //EV3BumperCar.leftMotor.
  }
}


class DetectWall implements Behavior
{

  public DetectWall()
  {
    //touch = new TouchSensor(SensorPort.S1);
    //sonar = new UltrasonicSensor(SensorPort.S3);
  }
  
  
  private boolean checkDistance()
  {

      int dist = EV3BumperCar.sensor.distance;
      if (dist < 30)
      {
          Button.LEDPattern(2);
          return true;
      }
      else
      {
          Button.LEDPattern(1);
          return false;
      }
  }
  
  public boolean takeControl()
  {
    return checkDistance();
  }

  public void suppress()
  {
    //Since  this is highest priority behavior, suppress will never be called.
  }

  public void action()
  {
      EV3BumperCar.leftMotor.rotate(-180, true);// start Motor.B rotating backward
      EV3BumperCar.rightMotor.rotate(-180);  // rotate C farther to make the turn
    if ((System.currentTimeMillis() & 0x1) != 0)
    {
        EV3BumperCar.leftMotor.rotate(-180, true);// start Motor.B rotating backward
        EV3BumperCar.rightMotor.rotate(180);  // rotate C farther to make the turn
    }
    else
    {
        EV3BumperCar.rightMotor.rotate(-180, true);// start Motor.B rotating backward
        EV3BumperCar.leftMotor.rotate(180);  // rotate C farther to make the turn        
    }
  }
  
}


