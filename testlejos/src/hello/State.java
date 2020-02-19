package hello;

public class State {
	private double x, y, lastTravelledDistance;
	private boolean isObstacle, isFloorAhead;
	private double clawAngle;
	
	public State() {} // Method overloading
	
	public State(double x, double y, double lastTravelledDistance, 
			boolean isObstacle, boolean isFloorAhead) {
		this.x = x;
		this.y = y;
		this.lastTravelledDistance = lastTravelledDistance;
		this.isObstacle = isObstacle;
		this.isFloorAhead = isFloorAhead;
	}

	public State(double x, double y, double lastTravelledDistance, boolean isObstacle, boolean isFloorAhead,
			double clawAngle) {
		this.x = x;
		this.y = y;
		this.lastTravelledDistance = lastTravelledDistance;
		this.isObstacle = isObstacle;
		this.isFloorAhead = isFloorAhead;
		this.clawAngle = clawAngle;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getLastTravelledDistance() {
		return lastTravelledDistance;
	}

	public void setLastTravelledDistance(double lastTravelledDistance) {
		this.lastTravelledDistance = lastTravelledDistance;
	}

	public boolean isObstacle() {
		return isObstacle;
	}

	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

	public boolean isFloorAhead() {
		return isFloorAhead;
	}

	public void setFloorAhead(boolean isFloorAhead) {
		this.isFloorAhead = isFloorAhead;
	}

	public double getClawAngle() {
		return clawAngle;
	}

	public void setClawAngle(double clawAngle) {
		this.clawAngle = clawAngle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(clawAngle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (isFloorAhead ? 1231 : 1237);
		result = prime * result + (isObstacle ? 1231 : 1237);
		temp = Double.doubleToLongBits(lastTravelledDistance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		State other = (State) obj;
		if (Double.doubleToLongBits(clawAngle) != Double.doubleToLongBits(other.clawAngle))
			return false;
		if (isFloorAhead != other.isFloorAhead)
			return false;
		if (isObstacle != other.isObstacle)
			return false;
		if (Double.doubleToLongBits(lastTravelledDistance) != Double.doubleToLongBits(other.lastTravelledDistance))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "State [x=" + x + ", y=" + y + ", dist=" + lastTravelledDistance + ", obst="
				+ isObstacle + ", floor=" + isFloorAhead + ", claw=" + clawAngle + "]";
	}
	
}
