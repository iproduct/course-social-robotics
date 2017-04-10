package org.iproduct.iptpi.domain.movement;

public class MotorsCommand {
	private int dirL, dirR, velocityL, velocityR;
	float remainingPath;

	public MotorsCommand(int dirL, int dirR, int velocityL, int velocityR, float remainingPath) {
		this.dirL = dirL;
		this.dirR = dirR;
		this.velocityL = velocityL;
		this.velocityR = velocityR;
		this.remainingPath = remainingPath;
	}

	public int getDirL() {
		return dirL;
	}

	public int getDirR() {
		return dirR;
	}

	public int getVelocityL() {
		return velocityL;
	}

	public int getVelocityR() {
		return velocityR;
	}

	public float getRemainingPath() {
		return remainingPath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dirL;
		result = prime * result + dirR;
		result = prime * result + velocityL;
		result = prime * result + velocityR;
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
		MotorsCommand other = (MotorsCommand) obj;
		if (dirL != other.dirL)
			return false;
		if (dirR != other.dirR)
			return false;
		if (velocityL != other.velocityL)
			return false;
		if (velocityR != other.velocityR)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MotorsCommand [dirL=" + dirL + ", dirR=" + dirR + ", velocityL=" + velocityL + ", velocityR="
				+ velocityR + ", remainingPath=" + remainingPath + "]";
	}

	
}
