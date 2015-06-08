package com.lefu.cmdtools.client.core.command;

import java.util.Properties;

import com.lefu.cmdtools.client.AbstractData;
import com.lefu.cmdtools.client.Data;
import com.lefu.cmdtools.client.core.AbstractCommand;
import com.lefu.cmdtools.client.net.Connection;

public class NormalCommand extends AbstractCommand {

	@Override
	public String execute(Properties props) {
		Connection conn = getConnection();
		NormalData data = new NormalData(convert2String(props));
		conn.write(data);
		String content = conn.read();
		return assemble(content);
	}
	
	public class NormalData extends AbstractData {
		private final String content;
		
		public NormalData(String content) {
			this.content = content;
			setCommand(Data.CMD_NORMAL);
		}
		
		@Override
		public String toString() {
			return content;
		}
	}
}
