package org.iproduct.iptpi.domain.movement;

public class MovementData {
	
	//private float curvature = 0; // radians/mm
	private float velocity = 40; // mm/sec
	private float angularVelocity = 0; // radians/sec
	private float acceleration = 0; // mm/sec^2
	private float angularAcceleration = 0; // radians/sec^2
	
	public MovementData(){
	}
	
	public MovementData(float velocity) {
		this.velocity = velocity;
	}

	public MovementData(float velocity, float angularVelocity, float acceleration, float angularAcceleration) {
		this.velocity = velocity;
		this.angularVelocity = angularVelocity;
		this.acceleration = acceleration;
		this.angularAcceleration = angularAcceleration;
	}

	public float getVelocity() {
		return velocity;
	}

	public float getAngularVelocity() {
		return angularVelocity;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public float getAngularAcceleration() {
		return angularAcceleration;
	}

	@Override
	public String toString() {
		return "RelativeMovement [velocity=" + velocity + ", angularVelocity=" + angularVelocity 
				+ ", acceleration=" + acceleration + ", angularacceleration=" + angularAcceleration + "]";
	}

}
