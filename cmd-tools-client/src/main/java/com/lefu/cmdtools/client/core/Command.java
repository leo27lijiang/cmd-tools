package com.lefu.cmdtools.client.core;

import java.util.Properties;

public interface Command {
	public String NULL = "__NULL__";
	
	public String SEPARATE = "|";
	
	public String INTERNAL_SEPARATE = "=";
	
	/**
	 * 执行命令，返回字符串响应
	 * @param props
	 * @return
	 */
	public String execute(Properties props);
	
}
