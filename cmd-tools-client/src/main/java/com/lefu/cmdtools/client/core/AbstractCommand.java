package com.lefu.cmdtools.client.core;

import java.util.Properties;

import com.lefu.cmdtools.client.net.Connection;

public abstract class AbstractCommand implements Command {
	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public String assemble(String content) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		buffer.append("Host " + getConnection().getRemote().toString() + " response:\n");
		buffer.append(content);
		buffer.append("\n");
		buffer.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		return buffer.toString();
	}
	
	public String convert2String(Properties props) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String key : props.stringPropertyNames()) {
			if (first) {
				first = false;
			} else {
				sb.append(SEPARATE);
			}
			sb.append(key);
			sb.append(INTERNAL_SEPARATE);
			String value = props.getProperty(key);
			if (value == null || value.trim().equals("")) {
				sb.append(NULL);
			} else {
				sb.append(value.trim());
			}
		}
		return sb.toString();
	}
}
