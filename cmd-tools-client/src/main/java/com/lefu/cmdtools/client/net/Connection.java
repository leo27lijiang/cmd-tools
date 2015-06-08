package com.lefu.cmdtools.client.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.lefu.cmdtools.client.Data;

/**
 * 连接定义
 * @author jiang.li
 *
 */
public interface Connection extends Closeable {
	public String SOCKET_SCHEME = "socket";
	
	/**
	 * 设置连接的目的主机信息
	 * @param host
	 * @param port
	 */
	public void setRemote(String host, int port);
	/**
	 * 设置链接的目的主机信息
	 * @param address
	 */
	public void setRemote(InetSocketAddress address);
	/**
	 * 获取目的主机信息
	 * @return
	 */
	public InetSocketAddress getRemote();
	/**
	 * 发起连接请求
	 * @throws IOException
	 */
	public void connect() throws IOException;
	/**
	 * 当前链接状态，True 已建立连接 False 未建立连接/已断开
	 * @return
	 */
	public boolean isConnected();
	/**
	 * 写入数据
	 * @param data
	 */
	public void write(Data data);
	/**
	 * 读取数据，响应统一使用字符串形式
	 * @return
	 */
	public String read();
}
