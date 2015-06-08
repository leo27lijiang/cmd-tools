package com.lefu.cmdtools.client.core.factory;

import com.lefu.cmdtools.client.core.Command;
import com.lefu.cmdtools.client.core.CommandFactory;
import com.lefu.cmdtools.client.core.command.NormalCommand;
import com.lefu.cmdtools.client.net.Connection;

public class NormalCommandFactory implements CommandFactory {

	@Override
	public Command buildCommand(Connection conn) {
		NormalCommand cmd = new NormalCommand();
		cmd.setConnection(conn);
		return cmd;
	}

}
