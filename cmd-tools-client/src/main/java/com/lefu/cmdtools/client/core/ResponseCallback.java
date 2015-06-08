package com.lefu.cmdtools.client.core;

/**
 * 响应处理
 * @author jiang.li
 *
 */
public interface ResponseCallback {
	
	public void onResponse(String response);
	
	public void onException(Throwable cause);
}
