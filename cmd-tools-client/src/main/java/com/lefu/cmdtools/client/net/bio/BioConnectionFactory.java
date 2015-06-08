package com.lefu.cmdtools.client.net.bio;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import com.lefu.cmdtools.client.net.Connection;
import com.lefu.cmdtools.client.net.ConnectionFactory;

/**
 * 解析 socket://host:port 协议建立socket连接
 * @author jiang.li
 *
 */
public class BioConnectionFactory implements ConnectionFactory {

	@Override
	public Connection getConnection(String uri) throws Exception {
		URI u = new URI(uri);
		if (!u.getScheme().equals(Connection.SOCKET_SCHEME)) {
			throw new URISyntaxException(uri, "Unkown scheme");
		}
		return new BioConnection(new InetSocketAddress(u.getHost(), u.getPort()));
	}

}
