package org.iproduct.cocktails.robot;

public interface RobotParametrs {
	public static final double MAIN_AXE_LENGTH = 165; //mm
	public static final double WHEEL_RADIUS = 34.5; // [mm]
	public static final double ENCODER_STEP_LENGTH = 0.4794; //mm
	public static final double MAX_ANGULAR_WHEEL_ACCELERATION = 6; // [rad / s^2]
	public static final double MAX_ROBOT_LINEAR_ACCELERATION = 
			MAX_ANGULAR_WHEEL_ACCELERATION * WHEEL_RADIUS / 2; // [mm / s^2]
	public static final double MAX_ROBOT_ANGULAR_ACCELERATION = 
			MAX_ANGULAR_WHEEL_ACCELERATION * WHEEL_RADIUS / MAIN_AXE_LENGTH; // [mm / s^2]
	public static final double MAX_ROBOT_ANGULAR_VELOCITY = 0.65; // [rad / s]
	public static final double MAX_ROBOT_LINEAR_VELOCITY = MAX_ROBOT_ANGULAR_VELOCITY * MAIN_AXE_LENGTH / 2; // [mm / s]
	public static final double ROBOT_STOPPING_DECCELERATION = 110; // [mm / s^2]

}
