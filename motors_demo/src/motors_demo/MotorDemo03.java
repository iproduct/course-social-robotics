package motors_demo;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.Touch;

public class MotorDemo03 {

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
		SensorMode touch = touchSensor.getTouchMode();
		float[] sample = new float[touch.sampleSize()];
		
		RegulatedMotor m = new EV3LargeRegulatedMotor(MotorPort.A);
		m.resetTachoCount();
		
		m.rotateTo(320, true);
		
//		int angle = m.getTachoCount(); // should be -360
//		lcd.drawInt(angle, 0, 0);
//		keys.waitForAnyPress();
		
//		m.rotateTo(-100, true);
//		do{
//			touch.fetchSample(sample, 0);
//		} while (sample[0] == 0);
		
		while (m.isMoving())
			Thread.yield();
		m.stop();	
		
		int angle = m.getTachoCount(); // should be < -100
		lcd.drawInt(angle, 0, 1);
//		keys.waitForAnyPress();
	}

}
