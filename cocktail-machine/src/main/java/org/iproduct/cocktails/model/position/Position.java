package org.iproduct.cocktails.model.position;

import java.text.SimpleDateFormat;

public class Position {
	private float x;
	private float y;
	private double heading;
	private long timestamp;
	
	public Position(float x, float y, double heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.timestamp = System.currentTimeMillis();
	}

	public Position(float x, float y, double heading, long timestamp) {
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.timestamp = timestamp;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public double getHeading() {
		return heading;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Position translate(float dx, float dy){
		return new Position(x + dx, y + dy, heading);
	}
	
	public Position rotate(double dHead){
		return new Position(x, y, heading + dHead );
	}
	
	public Position change(float dx, float dy, double dHead){
		return new Position(x + dx, y + dy, heading + dHead );
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss:SSS");
		return "Position [x=" + x + ", y=" + y + ", heading=" + heading + ", timestamp=" + sdf.format(timestamp) + "]";
	}
	
}
