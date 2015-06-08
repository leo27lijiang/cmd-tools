package com.lefu.cmdtools.client.core.command;

import java.util.Properties;

import com.lefu.cmdtools.client.AbstractData;
import com.lefu.cmdtools.client.Data;
import com.lefu.cmdtools.client.core.AbstractCommand;
import com.lefu.cmdtools.client.net.Connection;

public class ServerOptionList extends AbstractCommand {

	@Override
	public String execute(Properties props) {
		Connection conn = getConnection();
		conn.write(new ServerOptionListData());
		String content = conn.read();
		return assemble(content);
	}
	
	public class ServerOptionListData extends AbstractData {
		
		public ServerOptionListData() {
			setCommand(Data.CMD_SERVERLIST);
		}
		
		@Override
		public String toString() {
			return null;
		}
		
	}
	
}
