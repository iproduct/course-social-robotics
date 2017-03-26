package org.iproduct.iptpi.domain.arduino;

public enum ArduinoCommand {
	FOLLOW_LINE((byte)1),
	NOT_FOLLOW_LINE((byte)2); 
	
	private byte code;
	
	private ArduinoCommand(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
}
