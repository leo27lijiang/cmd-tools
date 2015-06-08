package com.lefu.cmdtools.server.net;

public class NetConfig {
	/**
	 * 允许的最大连接数
	 */
	public static final int MAX_CONNECTIONS = 5;
	/**
	 * Socket读取超时时间，单位秒
	 */
	public static final int MAX_TIMEOUT = 10;
	
	private int maxConnections = MAX_CONNECTIONS;
	private int maxTimeout = MAX_TIMEOUT;
	/**
	 * 默认不限制IP访问，多规则之间使用 , 分隔
	 */
	private String access = "*.*.*.*";
	private int bindPort = 13400;
	
	public int getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	public int getMaxTimeout() {
		return maxTimeout;
	}
	public void setMaxTimeout(int maxTimeout) {
		this.maxTimeout = maxTimeout;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public int getBindPort() {
		return bindPort;
	}
	public void setBindPort(int bindPort) {
		this.bindPort = bindPort;
	}
	
}
