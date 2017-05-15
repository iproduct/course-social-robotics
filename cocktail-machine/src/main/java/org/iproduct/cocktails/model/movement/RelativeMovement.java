package org.iproduct.cocktails.model.movement;

public class RelativeMovement extends MovementData{
	private float deltaX = 100; // mm
	private float deltaY = 100; // mm
	private float deltaHeading = 0; // radians
	
	public RelativeMovement() {
	}
	
	public RelativeMovement(float deltaHeading) {
		this.deltaHeading = deltaHeading;
	}

	public RelativeMovement(float deltaX, float deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public RelativeMovement(float deltaX, float deltaY, float deltaHeading) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaHeading = deltaHeading;
	}

	public RelativeMovement(float deltaX, float deltaY, float deltaHeading, float velocity) {
		super(velocity);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaHeading = deltaHeading;
	}

	public RelativeMovement(float deltaX, float deltaY, float deltaHeading, float velocity, float angularVelocity,
			float acceleration, float angularAcceleration) {
		super(velocity, angularVelocity, acceleration, angularAcceleration);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaHeading = deltaHeading;
	}

	public float getDeltaX() {
		return deltaX;
	}

	public float getDeltaY() {
		return deltaY;
	}

	public float getDeltaHeading() {
		return deltaHeading;
	}

	@Override
	public String toString() {
		return "RelativeMovement [deltaX=" + deltaX + ", deltaY=" + deltaY + ", curvature=" + deltaHeading + ", velocity="
				+ getVelocity() + ", angularVelocity=" + getAngularVelocity() + ", acceleration=" + getAcceleration()
				+ ", angularacceleration=" + getAngularAcceleration() + "]";
	}

}
