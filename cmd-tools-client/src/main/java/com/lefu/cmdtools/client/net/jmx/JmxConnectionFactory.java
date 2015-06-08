package com.lefu.cmdtools.client.net.jmx;

import com.lefu.cmdtools.client.net.Connection;
import com.lefu.cmdtools.client.net.ConnectionFactory;

public class JmxConnectionFactory implements ConnectionFactory {

	@Override
	public Connection getConnection(String uri) throws Exception {
		throw new RuntimeException("Not supported JMX connection now");
	}

}
