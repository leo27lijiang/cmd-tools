package com.lefu.cmdtools.client.core.factory;

import com.lefu.cmdtools.client.core.Command;
import com.lefu.cmdtools.client.core.CommandFactory;
import com.lefu.cmdtools.client.core.command.ServerOptionList;
import com.lefu.cmdtools.client.net.Connection;

public class ServerOptionListFactory implements CommandFactory {
	
	@Override
	public Command buildCommand(Connection conn) {
		ServerOptionList cmd = new ServerOptionList();
		cmd.setConnection(conn);
		return cmd;
	}

}
