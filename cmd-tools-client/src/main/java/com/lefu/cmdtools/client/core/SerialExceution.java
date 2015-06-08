package com.lefu.cmdtools.client.core;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import com.lefu.cmdtools.client.net.Connection;
import com.lefu.cmdtools.client.net.ConnectionFactory;

public class SerialExceution extends AbstractExecution {
	
	public SerialExceution() {
		
	}
	
	public SerialExceution(ConnectionFactory connectionFactory) {
		setConnectionFactory(connectionFactory);
	}
	
	@Override
	public void work(CommandFactory factory, Set<String> servers,
			Properties props, ResponseCallback callback) throws Exception {
		for (String address : servers) {
			Connection conn = getConnectionFactory().getConnection(address);
			try {
				conn.connect();
			} catch (IOException e) {
				e.printStackTrace();
				conn = null;
			}
			if (conn != null) {
				Command command = factory.buildCommand(conn);
				try {
					String response = command.execute(props);
					callback.onResponse(response);
				} catch (Exception e) {
					callback.onException(e);
				} finally {
					try {
						conn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
