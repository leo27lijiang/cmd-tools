package com.lefu.cmdtools.client.net;

public interface ConnectionFactory {
	
	/**
	 * 获取连接
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection(String uri) throws Exception;
	
}
