package com.lefu.cmdtools.client.core;

import com.lefu.cmdtools.client.net.Connection;

public interface CommandFactory {
	
	public Command buildCommand(Connection conn);
	
}
