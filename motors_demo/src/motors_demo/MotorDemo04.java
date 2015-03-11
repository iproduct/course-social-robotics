package motors_demo;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.Touch;
import lejos.utility.Delay;

public class MotorDemo04 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
		SensorMode color = colorSensor.getColorIDMode();
		float[] sample = new float[color.sampleSize()];
		color.fetchSample(sample, 0);
		int colorId = (int)sample[0];
		String colorName = "";
		switch(colorId){
			case Color.NONE: colorName = "NONE"; break;
			case Color.BLACK: colorName = "BLACK"; break;
			case Color.BLUE: colorName = "BLUE"; break;
			case Color.GREEN: colorName = "GREEN"; break;
			case Color.YELLOW: colorName = "YELLOW"; break;
			case Color.RED: colorName = "RED"; break;
			case Color.WHITE: colorName = "WHITE"; break;
			case Color.BROWN: colorName = "BROWN"; break;
		}
		lcd.drawString(colorId + " - " + colorName, 0, 0);
		keys.waitForAnyPress();

//		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
//		SensorMode touch = touchSensor.getTouchMode();
//		float[] sample = new float[touch.sampleSize()];
//		
//		RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
//		RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
//		RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
//		mA.resetTachoCount();
//		mB.resetTachoCount();
//		mC.resetTachoCount();
//		
//		mA.rotateTo(760);
//		int angle = mA.getTachoCount(); // should be -360
//		lcd.drawInt(angle, 0, 0);
//		keys.waitForAnyPress();
//		
//		mB.setSpeed(720);// 2 RPM
//		mC.setSpeed(720);
//		mB.forward();
//		mC.forward();
//		Delay.msDelay(1000);
//		mB.stop();
//		mC.stop();
//		mB.rotateTo(360);
//		mB.rotate(-720, true);
//		while (mB.isMoving())
//			Thread.yield();
//		angle = mB.getTachoCount(); 
//		lcd.drawInt(angle, 0, 1);
//		
//		mA.rotateTo(-100, true);
//		do{
//			touch.fetchSample(sample, 0);
//		} while (mA.isMoving() && sample[0] == 0);
//		mA.stop();	
//		
//		angle = mA.getTachoCount(); // should be -360
//		lcd.drawInt(angle, 0, 2);
//		keys.waitForAnyPress();
	}
}
