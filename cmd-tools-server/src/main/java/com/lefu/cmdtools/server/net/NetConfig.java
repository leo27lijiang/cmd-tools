package com.lefu.cmdtools.server.net;

public class NetConfig {
	public static final int MAX_CONNECTIONS = 5;
	
	public static final int MAX_TIMEOUT = 10;
	
	/**
	 * 允许的最大连接数
	 */
	private int maxConnections = MAX_CONNECTIONS;
	/**
	 * Socket读取超时时间，单位秒
	 */
	private int maxTimeout = MAX_TIMEOUT;
	/**
	 * 默认不限制IP访问，多规则之间使用 , 分隔 * 代表匹配所有
	 * 类似192.168.13.*这样的规则
	 */
	private String access = "*.*.*.*";
	/**
	 * 监听的端口
	 */
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
