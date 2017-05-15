package org.iproduct.cocktails.model.movement;

public class ForwardMovement extends MovementData{
	private float distance = 100; // mm
	
	public ForwardMovement() {
	}
	
	public ForwardMovement(float distance) {
		this.distance = distance;
	}

	public ForwardMovement(float distance, float velocity) {
		super(velocity);
		this.distance = distance;
	}

	public ForwardMovement(float distance, float velocity, float acceleration) {
		super(velocity, 0, acceleration, 0);
		this.distance = distance;
	}

	public float getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return "RelativeMovement [distance=" + distance + ", velocity="
				+ getVelocity() + ", acceleration=" + getAcceleration() + "]";
	}

}
