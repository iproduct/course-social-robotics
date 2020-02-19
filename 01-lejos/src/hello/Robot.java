package hello;

import java.util.Arrays;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
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
	
	private EV3 ev3 = (EV3) BrickFinder.getLocal();
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
	
	private State state;

	public Robot(State initialState) {
		this.state = initialState;
		colorSensor.setCurrentMode(colorSensor.getRGBMode().getName());
		SensorMode mode = colorSensor.getRGBMode();
		samples = new float[mode.sampleSize()];
		touchSensor.setCurrentMode(touchSensor.getTouchMode().getName());
	}
	
	//getters and setters
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public EV3 getEv3() {
		return ev3;
	}

	public TextLCD getLcd() {
		return lcd;
	}

	public Keys getKeys() {
		return keys;
	}

	public RegulatedMotor getMotorA() {
		return mA;
	}

	public RegulatedMotor getMotorB() {
		return mB;
	}

	public RegulatedMotor getMotorC() {
		return mC;
	}

	public EV3ColorSensor getColorSensor() {
		return colorSensor;
	}

	public EV3TouchSensor getTouchSensor() {
		return touchSensor;
	}

	// business methods
	public MovementStatus execute(Behavior behavior) {
		return behavior.execute(this);
	}

	public void moveForward(double centimeters) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		mC.resetTachoCount();
		int motorDegrees = (int) (MOTOR_DEGREES_TO_CENTIMETERS_RATIO * centimeters);
		mB.rotate(motorDegrees, true);
		mC.rotate(motorDegrees);
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO) );
	}

	public MovementStatus moveForwardWhileTableAndNoObstacle(double centimeters) {
		return moveForwardWhileNoColorAndNoObstacle(centimeters, Color.BLACK);
	}
		
	public MovementStatus moveForwardWhileNoColorAndNoObstacle(double centimeters, Color color) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (MOTOR_DEGREES_TO_CENTIMETERS_RATIO * centimeters);
		mB.rotate(motorDegrees, true);
		mC.rotate(motorDegrees, true);
		
		// Go while there is footing
		float[] rgb;
		boolean isFootingAhead, isObstacle;
		Color colorAhead;
		do {
//			printRGBColor(rgb);
			printTachoCounts();
			colorAhead = getColorAhead();
			isObstacle = isTouchingObstacle();
		} while(mB.isMoving() && mC.isMoving() && colorAhead != color && !isObstacle);
		
		// else stop
		mB.stop(true);
		mC.stop(true);
		Sound.beep();
		
		// calculate distance
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
		
		if(colorAhead == color) return MovementStatus.STOP_COLOR;
		else if(isObstacle) return MovementStatus.OBSTACLE;
		else return MovementStatus.OK;
	}

	public void moveBackward(double centimeters) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (MOTOR_DEGREES_TO_CENTIMETERS_RATIO * centimeters);
		mB.rotate(-motorDegrees, true);
		mC.rotate(-motorDegrees);
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
	}

	public void turnRight(double degrees) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(motorDegrees, true);
		mC.rotate(-motorDegrees);
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
	}

	public void turnLeft(double degrees) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(-motorDegrees, true);
		mC.rotate(motorDegrees);
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
	}

	public MovementStatus turnLeftWhileNotColor(double degrees, Color color) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(-motorDegrees, true);
		mC.rotate(motorDegrees, true);
		Color colorAhead;
		do {
			colorAhead = getColorAhead();
		} while ((mB.isMoving() || mC.isMoving()) && colorAhead != color);
		mB.stop(true);
		mC.stop(true);
		mB.rotate(20, true);
		mC.rotate(-20, true);
		Sound.beep();
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
		return (colorAhead == color) ? MovementStatus.STOP_COLOR : MovementStatus.OK;
	}

	public MovementStatus turnRightWhileNotColor(double degrees, Color color) {
		double tachoB = mB.getTachoCount();
		double tachoC = mC.getTachoCount();
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(motorDegrees, true);
		mC.rotate(-motorDegrees, true);
		Color colorAhead;
		do {
			colorAhead = getColorAhead();
		} while ((mB.isMoving() || mC.isMoving()) && colorAhead != color);
		mB.stop(true);
		mC.stop(true);
		mB.rotate(-20, true);
		mC.rotate(20, true);
		Sound.beep();
		state.setLastTravelledDistance( Math.abs(mB.getTachoCount() - tachoB 
				+ mC.getTachoCount() - tachoC) / 
				(2 * MOTOR_DEGREES_TO_CENTIMETERS_RATIO));
		return (colorAhead == color) ? MovementStatus.STOP_COLOR : MovementStatus.OK;
	}

	public void grable(double degrees) {
		mA.rotate((int) (6.8 * degrees));
	}

	public void goSqare(double side) {
		for (int i = 0; i < 4; i++) {
			moveForward(side);
			turnRight(90);
		}
	}

	// Sensor methods

	public float[] sampleRGBColor() {
		colorSensor.fetchSample(samples, 0);
		return samples;
	}
	
	public Color getColorAhead() {
		float[] rgb = sampleRGBColor();
		System.out.println(Arrays.toString(rgb));
		if (rgb[0] < 0.015 && rgb[1] < 0.015 && rgb[2] < 0.015) {
			return Color.BLACK;
		} else if (rgb[0] > 0.07) {
			return Color.RED;
		} else if (rgb[2] > 0.07) {
			return Color.BLUE;
		} else {
			return Color.OTHER;
		}
	}

	public boolean isTouchingObstacle() {
		touchSensor.fetchSample(samples, 0);
		return samples[0] == 1;
	}

	public void printRGBColor(float[] rgb) {
		lcd.drawString("R: " + rgb[0], 0, 3);
		lcd.drawString("G: " + rgb[1], 0, 4);
		lcd.drawString("B: " + rgb[2], 0, 5);
	}
	
	public void printTachoCounts() {
		lcd.drawString("MA: " + mA.getTachoCount(), 0, 3);
		lcd.drawString("MB: " + mB.getTachoCount(), 0, 4);
		lcd.drawString("MC: " + mC.getTachoCount(), 0, 5);
	}
	
	public void printState() {
		lcd.drawString("Distance: " + getState().getLastTravelledDistance(), 0, 1);
	}

	public boolean isFootingAhead(float[] rgb) {
		return rgb[0] > 0.015 // Red color
				|| rgb[1] > 0.015 // Green color
				|| rgb[2] > 0.015; // Blue color
	}
	
	public void monitorObstacle() {
		boolean isObstacle;
		do {
			Delay.msDelay(20);
			isObstacle = isTouchingObstacle();
			lcd.drawString("Touch: " + isObstacle, 0, 2);
		} while (!isObstacle);
		Sound.buzz();
	}

	public static void main(String[] args) {
		Robot robot = new Robot(new State());
//		robot.execute(new ObstacleAvoidanceBehavior(100));
		robot.execute(new LineFollowBehavior(100));
		
		
//		robot.turnRightWhileNotColor(360, Color.RED);	
//		robot.lcd.drawString("Color: " + robot.getColorAhead(), 0, 2);
//		robot.execute(new ObstacleAvoidanceBehavior(100));
//		robot.moveForwardWhileTableAndNoObstacle(50);
//		robot.moveForwardWhileFooting(50);
//		float[] rgb = robot.sampleRGBColor();
//		robot.printRGBColor(rgb);
		robot.keys.waitForAnyPress(50000);
	}

}
