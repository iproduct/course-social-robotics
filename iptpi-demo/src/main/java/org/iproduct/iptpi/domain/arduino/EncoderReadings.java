package org.iproduct.iptpi.domain.arduino;

public class EncoderReadings {
	int encoderL;
	int encoderR;
	long timestamp;
	
	public EncoderReadings(int l, int r, long estimatedTimestamp) {
		this.encoderL = l;
		this.encoderR = r;
		timestamp = estimatedTimestamp;
	}

	public int getEncoderR() {
		return encoderR;
	}

	public int getEncoderL() {
		return encoderL;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + encoderR;
		result = prime * result + encoderL;
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
		EncoderReadings other = (EncoderReadings) obj;
		if (encoderR != other.encoderR)
			return false;
		if (encoderL != other.encoderL)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EncoderReadings [encoderL=" + encoderL + ", encoderR=" + encoderR + ", timestamp=" + timestamp + "]";
	}

	
}
