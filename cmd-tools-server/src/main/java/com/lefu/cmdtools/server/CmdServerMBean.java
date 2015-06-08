package com.lefu.cmdtools.server;

import java.util.Map;

/**
 * 用于JMX的标准调用接口定义
 * <pre>
 * 请使用这种约定的 <code>ObjectName</code> 来注册 MBean
 * com.lefu.cmdtools.server:type=CmdServer,name=CmdTool
 * </pre>
 * @author jiang.li
 *
 */
public interface CmdServerMBean {
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
