package org.iproduct.iptpi.domain.movement;

public class AbsoluteMovement extends MovementData {
	private float x = 0;
	private float y = 0;
	private float heading = 0;
	
	public AbsoluteMovement() {
	}

	public AbsoluteMovement(float heading) {
		this.heading = heading;
	}

	public AbsoluteMovement(float absoluteX, float absoluteY) {
		this.x = absoluteX;
		this.y = absoluteY;
	}

	public AbsoluteMovement(float absoluteX, float absoluteY, float curvature) {
		super(curvature);
		this.x = absoluteX;
		this.y = absoluteY;
	}

	public AbsoluteMovement(float absoluteX, float absoluteY, float curvature, float velocity) {
		super(0, 0, curvature, velocity);
		this.x = absoluteX;
		this.y = absoluteY;
	}
	
	public AbsoluteMovement(float absoluteX, float absoluteY, float curvature, float velocity, float heading) {
		super(0, 0, curvature, velocity);
		this.x = absoluteX;
		this.y = absoluteY;
		this.heading = heading;
	}

	public AbsoluteMovement(float absoluteX, float absoluteY, float curvature, float velocity, float angularVelocity,
			float heading, float acceleration, float angularacceleration) {
		super(velocity, angularVelocity, acceleration, angularacceleration);
		this.x = absoluteX;
		this.y = absoluteY;
		this.heading = heading;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbsoluteMovement [x=").append(x).append(", y=").append(y).append(", heading=").append(heading)
				.append(", velocity()=").append(getVelocity())
				.append(", angularVelocity()=").append(getAngularVelocity()).append(", acceleration()=")
				.append(getAcceleration()).append(", angularAcceleration()=").append(getAngularAcceleration())
				.append("]");
		return builder.toString();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getHeading() {
		return heading;
	}

}
