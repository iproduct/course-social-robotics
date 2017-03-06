package hello;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MoveDemo02 {
	private EV3 ev3 = (EV3) BrickFinder.getLocal();
	private TextLCD lcd = ev3.getTextLCD();
	private Keys keys = ev3.getKeys();

	private RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A), 
			mB = new EV3LargeRegulatedMotor(MotorPort.B),
			mC = new EV3LargeRegulatedMotor(MotorPort.C);

	public void run() {
		lcd.drawString("Hello Trayan", 4, 2);
		lcd.drawString("from leJOS", 4, 3);

		// config motors
		mB.setSpeed(900); // Degree per second
		mC.setSpeed(900);
		mB.synchronizeWith(new RegulatedMotor[] { mC });
		
//		for (int i = 0; i < 4; i++) {
//			moveForward(2000); //2s
//			turnLeft(520);
//		}
		moveBackward(2000); //2s

	}
	
	public void moveForward(int timeMs) {
		mB.startSynchronization();
		mB.forward();
		mC.forward();
		mB.endSynchronization();
		Delay.msDelay(timeMs);
		mB.startSynchronization();
		mB.stop();
		mC.stop();
		mB.endSynchronization();
	}

	public void moveBackward(int timeMs) {
		mB.startSynchronization();
		mB.backward();
		mC.backward();
		mB.endSynchronization();
		Delay.msDelay(timeMs);
		mB.startSynchronization();
		mB.stop();
		mC.stop();
		mB.endSynchronization();
	}
	
	public void turnLeft(int degrees) {
		mB.resetTachoCount();
		mC.resetTachoCount();
		
		mB.rotateTo(degrees, true);
		mC.rotateTo(-degrees, false);
	}


	public static void main(String[] args) {
		MoveDemo02 demo = new MoveDemo02();
		demo.run();
		

		// keys.waitForAnyPress(5000);
	}

}
