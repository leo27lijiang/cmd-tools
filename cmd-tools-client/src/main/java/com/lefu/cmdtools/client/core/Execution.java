package com.lefu.cmdtools.client.core;

import java.util.Properties;
import java.util.Set;

public interface Execution {
	
	/**
	 * 构建命令，调度命令执行
	 * @param factory 命令工厂
	 * @param servers 服务端URI列表
	 * @param props 命令属性
	 * @param callback 响应回调
	 * @throws Exception
	 */
	public void work(CommandFactory factory, Set<String> servers, Properties props, ResponseCallback callback) throws Exception;
	
}
