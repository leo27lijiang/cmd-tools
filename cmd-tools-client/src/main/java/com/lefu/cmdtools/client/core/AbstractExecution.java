package com.lefu.cmdtools.client.core;

import com.lefu.cmdtools.client.net.ConnectionFactory;

public abstract class AbstractExecution implements Execution {
	private ConnectionFactory connectionFactory;

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
}
