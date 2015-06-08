package com.lefu.cmdtools.client;

public abstract class AbstractData implements Data {
	private byte command = Data.CMD_NORMAL;
	
	@Override
	public byte getVersion() {
		return Data.VERSION_V1;
	}

	@Override
	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

}
