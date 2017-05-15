package org.iproduct.cocktails.model.arduino;

public class LineReadings {
	int lineL;
	int lineM;
	int lineR;
	long timestamp;
	
	public LineReadings(int l, int m, int r, long estimatedTimestamp) {
		this.lineL = l;
		this.lineM = m;
		this.lineR = r;
		timestamp = estimatedTimestamp;
	}

	public int getLineL() {
		return lineL;
	}

	public int getLineM() {
		return lineM;
	}

	public int getLineR() {
		return lineR;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineL;
		result = prime * result + lineM;
		result = prime * result + lineR;
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
		LineReadings other = (LineReadings) obj;
		if (lineL != other.lineL)
			return false;
		if (lineM != other.lineM)
			return false;
		if (lineR != other.lineR)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LineReadings [lineL=" + lineL + ", lineM=" + lineM + ", lineR=" + lineR + ", timestamp=" + timestamp
				+ "]";
	}

	
}
