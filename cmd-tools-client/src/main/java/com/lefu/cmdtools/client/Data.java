package com.lefu.cmdtools.client;

public interface Data {
	/**
	 * 协议版本定义
	 */
	public byte VERSION_V1 = 1;
	/**
	 * 用户定义命令调用
	 */
	public byte CMD_NORMAL = 1;
	/**
	 * 查看服务端命令列表
	 */
	public byte CMD_SERVERLIST = 2;
	/**
	 * 获取版本号
	 * @return
	 */
	public byte getVersion();
	/**
	 * 获取命令类型
	 * @return
	 */
	public byte getCommand();
	
}
