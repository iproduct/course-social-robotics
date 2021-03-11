package hello;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robot {
	private static final double MOTOR_DEGREES_TO_CENTIMETERS_RATIO = 35;
	
	private EV3 ev3 = (EV3) BrickFinder.getDefault();
	private TextLCD lcd = ev3.getTextLCD();
	private Keys keys = ev3.getKeys();
	// Motors
	private RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
	private RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
	private RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
	// Sensors
	private EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
	private EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
	private float[] samples;
	
	public Robot() {
//		this.state = initialState;
		colorSensor.setCurrentMode(colorSensor.getRGBMode().getName());
		SensorMode mode = colorSensor.getRGBMode();
		samples = new float[mode.sampleSize()];
		touchSensor.setCurrentMode(touchSensor.getTouchMode().getName());
		mB.setSpeed(150);// degrees per second
		mC.setSpeed(150);
		mB.synchronizeWith(new RegulatedMotor[] { mC });
	}

	
	public void moveForward(double centimeters) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		mC.resetTachoCount();
		int motorDegrees = (int) (MOTOR_DEGREES_TO_CENTIMETERS_RATIO * centimeters);
		mB.rotate(motorDegrees, true);
		mC.rotate(motorDegrees);
//		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
//				+ mC.getTachoCount() - tachoC) / 
//				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO) );
	}
	
	public void moveBackward(double centimeters) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (MOTOR_DEGREES_TO_CENTIMETERS_RATIO * centimeters);
		mB.rotate(-motorDegrees, true);
		mC.rotate(-motorDegrees);
//		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
//				+ mC.getTachoCount() - tachoC) / 
//				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
	}

	public void turnRight(double degrees) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(motorDegrees, true);
		mC.rotate(-motorDegrees);
//		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
//				+ mC.getTachoCount() - tachoC) / 
//				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
	}

	public void turnLeft(double degrees) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(-motorDegrees, true);
		mC.rotate(motorDegrees);
//		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
//				+ mC.getTachoCount() - tachoC) / 
//				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
	}


	public static void main(String[] args) {
		Robot r = new Robot();
		
		
//		Audio audio = ev3.getAudio();
//		audio.systemSound(0);
		
		Sound.beepSequenceUp();
		GraphicsLCD g = r.ev3.getGraphicsLCD();
		final int SW = g.getWidth();
		final int SH = g.getHeight();
		g.setFont(Font.getLargeFont());
		g.drawString("leJOS/EV3", SW/2 , SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
		Button.LEDPattern(3);
		r.mA.rotateTo((int) 6.8 *90 );
		r.moveForward(20);
		r.turnRight(180);
		
		Button.LEDPattern(5);
		r.moveForward(20);
		r.turnRight(180);
		r.mA.rotateTo((int) - 6.8 *90);
		Sound.beepSequence();
		Delay.msDelay(500);
		Button.LEDPattern(0);
		
	}
	
	

}
