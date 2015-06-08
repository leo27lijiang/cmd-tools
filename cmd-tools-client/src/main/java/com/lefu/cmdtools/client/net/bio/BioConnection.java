package com.lefu.cmdtools.client.net.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.lefu.cmdtools.client.Data;
import com.lefu.cmdtools.client.net.Connection;

/**
 * 采用协议：2(报文长度，不包含本身)+1(版本)+1(命令)+2(内容长度)+N(内容)
 * @author jiang.li
 *
 */
public class BioConnection implements Connection {
	private Socket socket;
	private InetSocketAddress address;
	
	public BioConnection() {
		
	}
	
	public BioConnection(InetSocketAddress address) {
		this.address = address;
	}
	
	@Override
	public void close() throws IOException {
		if (this.socket != null) {
			this.socket.close();
			this.socket = null;
		}
	}

	@Override
	public void setRemote(String host, int port) {
		setRemote(new InetSocketAddress(host, port));
	}

	@Override
	public void setRemote(InetSocketAddress address) {
		this.address = address;
	}
	
	@Override
	public InetSocketAddress getRemote() {
		return this.address;
	}

	@Override
	public void connect() throws IOException {
		this.socket = new Socket();
		try {
			this.socket.connect(address);
		} catch (IOException e) {
			close();
			throw e;
		}
	}

	@Override
	public boolean isConnected() {
		if (this.socket == null) {
			return false;
		}
		return this.socket.isConnected();
	}

	@Override
	public void write(Data data) {
		if (this.socket == null) {
			return;
		}
		String content = data.toString();
		ByteBuffer buffer = null;
		if (content == null) {
			buffer = ByteBuffer.allocate(6);
			buffer.putShort((short)4);
			buffer.put(data.getVersion());
			buffer.put(data.getCommand());
			buffer.putShort((short)0);
			buffer.flip();
		} else {
			byte[] d = content.getBytes(Charset.forName("UTF-8"));
			buffer = ByteBuffer.allocate(6+d.length);
			buffer.putShort((short)(d.length + 4));
			buffer.put(data.getVersion());
			buffer.put(data.getCommand());
			buffer.putShort((short)d.length);
			buffer.put(d);
			buffer.flip();
		}
		try {
			this.socket.getOutputStream().write(buffer.array());
			this.socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String read() {
		if (this.socket == null) {
			return null;
		}
		byte[] content = new byte[4096];
		try {
			int readed = this.socket.getInputStream().read(content);
			if (readed == -1) {
				return null;
			}
			return new String(content, 0, readed, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
