package com.lefu.cmdtools.client.core;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import com.lefu.cmdtools.client.net.Connection;
import com.lefu.cmdtools.client.net.ConnectionFactory;

public class ParallelExecution extends AbstractExecution {
	
	public ParallelExecution() {
		
	}
	
	public ParallelExecution(ConnectionFactory connectionFactory) {
		setConnectionFactory(connectionFactory);
	}
	
	@Override
	public void work(CommandFactory factory, Set<String> servers,
			Properties props, ResponseCallback callback) throws Exception{
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
				Thread t = new Thread(new CommandExecute(command, props, conn, callback));
				t.start();
			}
		}
	}
	
	public class CommandExecute implements Runnable {
		private final Command command;
		private final Properties props;
		private final Connection connection;
		private final ResponseCallback callback;
		
		public CommandExecute(Command command, Properties props, Connection conn, ResponseCallback callback) {
			this.command = command;
			this.props = props;
			this.connection = conn;
			this.callback = callback;
		}
		
		@Override
		public void run() {
			try {
				String response = command.execute(props);
				callback.onResponse(response);
			} catch (Exception e) {
				callback.onException(e);
			} finally {
				try {
					this.connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
