package com.lefu.cmdtools.server;

import java.util.Map;

/**
 * socket协议接口定义
 * @author jiang.li
 *
 */
public interface CmdServer {
	/**
	 * 打印应用参数列表
	 * @return
	 */
	public String optionList();
	/**
	 * 普通调用
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String normal(Map<String, String> params) throws Exception;
	
}
