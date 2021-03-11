package org.lejos.ev3.sample.pilottest;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;
import lejos.robotics.navigation.MovePilot;


/**
 * Unit test for a  pilot
 * @author Aswin Bouwmeester
 *
 */
public class TestPilot {
  static final int OLD_DIFFERENTIAL = 0;
  static final int NEW_DIFFERENTIAL = 1;
  static final int NEW_HOLONOMIC = 2;
  
  ArcRotateMoveController  pilot;
  PoseProvider poseProvider;
  double radius = 300;  
  double angle = 90;
  double distance = 1000;
  int time = 4000;
  private boolean wait = false;

  public static void main(String[] args) {
    TestPilot foo = new TestPilot(NEW_HOLONOMIC);
    foo.setDefaults();
    Sound.beep();
    Button.waitForAnyPress();
    foo.travel();
    foo.arc();
    foo.dynamics();
    Button.waitForAnyPress();
  }
  
private void setDefaults() {
  pilot.setLinearSpeed(pilot.getMaxLinearSpeed()/2);
  pilot.setLinearAcceleration(pilot.getMaxLinearSpeed()/4);
  pilot.setAngularSpeed(pilot.getMaxAngularSpeed()/2);
  pilot.setAngularAcceleration(pilot.getMaxAngularSpeed()/4);
}
  
private void endMove() {
  System.out.println(poseProvider.getPose()); 
  poseProvider.setPose(new Pose(0,0,0)); 
  if (wait ) Button.waitForAnyPress(time);

}

private TestPilot(int type) {
    
    switch (type) {
      case OLD_DIFFERENTIAL: {
        pilot = new DifferentialPilot(43.2, 142, Motor.D, Motor.A);
        poseProvider = new OdometryPoseProvider(pilot);
        break;}
      case NEW_DIFFERENTIAL: {
        Chassis chassis;
        Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 94.2).offset(57).invert(true);
        Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 94.2).offset(-57).invert(true);
        chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL);
        pilot = new MovePilot(chassis);
        poseProvider = chassis.getPoseProvider();
        radius = Math.max(radius, pilot.getMinRadius());
        poseProvider = new OdometryPoseProvider(pilot);
        break;
      }
        case NEW_HOLONOMIC: {
          Chassis chassis;
          Wheel wheel1 = WheeledChassis.modelHolonomicWheel(Motor.A, 48).polarPosition(0, 135).gearRatio(2);
          Wheel wheel2 = WheeledChassis.modelHolonomicWheel(Motor.B, 48).polarPosition(120, 135).gearRatio(2);
          Wheel wheel3 = WheeledChassis.modelHolonomicWheel(Motor.C, 48).polarPosition(240, 135).gearRatio(2);
          chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2, wheel3}, WheeledChassis.TYPE_HOLONOMIC);
          pilot = new MovePilot(chassis);
          poseProvider = chassis.getPoseProvider();
          radius = Math.max(radius, pilot.getMinRadius());
          poseProvider = new OdometryPoseProvider(pilot);
          break;
        }
      }
    }
    


private void travel() {
  pilot.travel(distance);
  endMove();
  pilot.travel(-distance);
  endMove();
  pilot.travel(distance, true);
  while(pilot.isMoving()) Delay.msDelay(10);
  endMove();
  pilot.travel(-distance, true);
  while(pilot.isMoving()) Delay.msDelay(10);
  endMove();
  pilot.travel(0, true);
  while(pilot.isMoving());
  endMove();
}

private void arc() {
  pilot.arc(0, 360);
  endMove();
  pilot.arc(0, -360);
  endMove();
  pilot.arc(radius, angle);
  endMove();
  pilot.arc(radius, -angle);
  endMove();
  pilot.arc(-radius, angle);
  endMove();
  pilot.arc(-radius, -angle);
  endMove();
  pilot.arc(radius, angle, true);
  while(pilot.isMoving()) Delay.msDelay(10);
  endMove();
  pilot.arc(radius, -angle, true);
  while(pilot.isMoving()) Delay.msDelay(10);
  endMove();
}


private void dynamics() {
  double lMax = pilot.getMaxLinearSpeed();
  
  for (double s =1 ; s<=4 ; s *= 2) {
    pilot.setLinearSpeed(lMax / s);
    for (double a =0.5 ; a<=4 ; a *= 2) {
      pilot.setLinearAcceleration(lMax / a);
      pilot.travel(distance);
      pilot.travel(-distance);
      endMove();
    }    
  }
  setDefaults();
}
}
