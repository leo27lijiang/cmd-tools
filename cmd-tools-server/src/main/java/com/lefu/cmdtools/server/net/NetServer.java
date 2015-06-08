package com.lefu.cmdtools.server.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.cmdtools.server.CmdServer;

/**
 * socket 协议服务端
 * @author jiang.li
 *
 */
public class NetServer {
	public static final byte CURRENT_VERSION = 1;
	private final static Logger log = LoggerFactory.getLogger(NetServer.class);
	private CmdServer cmdServer;
	private NetConfig netConfig;
	private ExecutorService executor;
	private ServerSocket serverSocket;
	private ServerSocketThread serverSocketThread;
	private AtomicInteger count = new AtomicInteger(0);
	private AtomicBoolean state = new AtomicBoolean(false);
	private String[] accessList;
	
	public NetServer() {
		this.netConfig = new NetConfig();
	}
	
	public NetServer(CmdServer cmdServer) {
		this();
		this.cmdServer = cmdServer;
	}
	
	public NetServer(CmdServer cmdServer, NetConfig netConfig) {
		this.cmdServer = cmdServer;
		this.netConfig = netConfig;
	}
	
	public void start() {
		if (state.get()) {
			return;
		}
		if (this.cmdServer == null) {
			throw new RuntimeException("CmdServer callback can not be null");
		}
		String ips = netConfig.getAccess();
		List<String> ipList = new ArrayList<String>();
		for (String i : ips.split("[,]")) {
			if (AccessUtil.isIP(i)) {
				ipList.add(i);
			}
		}
		accessList = new String[ipList.size()];
		for (int i = 0; i < ipList.size(); i++) {
			accessList[i] = ipList.get(i);
		}
		try {
			serverSocket = new ServerSocket(netConfig.getBindPort());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Server bind port error");
		}
		serverSocketThread = new ServerSocketThread(serverSocket, netConfig, log);
		serverSocketThread.setName(ServerSocketThread.class.getSimpleName());
		executor = Executors.newCachedThreadPool(new CmdToolsThreadFactory());
		serverSocketThread.start();
		state.set(true);
		log.info("CmdTools server is started, listen port at {}", netConfig.getBindPort());
	}
	
	public void shutdown() {
		if (!state.get()) {
			return;
		}
		executor.shutdown();
		try {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
		state.set(false);
		log.info("CmdTools server is shutdown.");
	}
	
	void newSocket(Socket socket) {
		if (count.get() >= netConfig.getMaxConnections()) {
			log.warn("Connection is full with {} , close new connections.", netConfig.getMaxConnections());
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		String ip = socket.getInetAddress().getHostAddress();
		if (!AccessUtil.testAccess(accessList, ip)) {
			log.warn("Connection from {} is forbidden", ip);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			executor.submit(new WorkThread(this, socket, cmdServer, log));
			count.getAndIncrement();
		} catch (RejectedExecutionException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void socketClosed() {
		this.count.decrementAndGet();
	}

	public void setCmdServer(CmdServer cmdServer) {
		this.cmdServer = cmdServer;
	}

	public void setNetConfig(NetConfig netConfig) {
		this.netConfig = netConfig;
	}
	
	public static void main(String[] args) {
		NetServer server = new NetServer();
		NetConfig config = new NetConfig();
		config.setBindPort(13400);
		server.setNetConfig(config);
		server.setCmdServer(new CmdServer(){

			@Override
			public String optionList() {
				return "Hello=you";
			}

			@Override
			public String normal(Map<String, String> params) throws Exception {
				return "Params size:" + params.size();
			}
			
		});
		server.start();
	}
	
	public class ServerSocketThread extends Thread {
		private final Logger log;
		private final ServerSocket serverSocket;
		private final NetConfig netConfig;
		
		public ServerSocketThread(ServerSocket serverSocket, NetConfig netConfig, Logger log) {
			this.serverSocket = serverSocket;
			this.netConfig = netConfig;
			this.log = log;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					Socket socket = this.serverSocket.accept();
					log.info("New connection from {}", socket.getRemoteSocketAddress());
					socket.setSoTimeout(netConfig.getMaxTimeout()*1000);
					newSocket(socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (serverSocket.isClosed()) {
					log.info("ServerSocket is closed, Thread will be finish.");
					break;
				}
			}
		}
	}
	
	public class WorkThread implements Runnable {
		private final Logger log;
		private final NetServer netServer;
		private final Socket socket;
		private final CmdServer cmdServer;
		
		public WorkThread(NetServer netServer, Socket socket, CmdServer cmdServer, Logger log) {
			this.netServer = netServer;
			this.socket = socket;
			this.cmdServer = cmdServer;
			this.log = log;
		}
		
		@Override
		public void run() {
			try {
				byte[] temp = new byte[1024];
				byte command = 0;
				String data = null;
				try {
					int readed = read(temp, this.socket.getInputStream());
					if (readed < 6) {
						log.warn("Unkown packet length {}", readed);
						return;
					}
					ByteBuffer buff = ByteBuffer.wrap(temp);
					buff.limit(readed);
					buff.position(0);
					short total = buff.getShort();
					byte version = buff.get();
					command = buff.get();
					short len = buff.getShort();
					if (version != CURRENT_VERSION) {
						log.error("Unsupported version {}", version);
						return;
					}
					ByteBuffer content = ByteBuffer.allocate(total);
					content.put(temp, 2, readed - 2);
					boolean needMoreData = readed < (total+2);
					while (needMoreData) {
						readed = read(temp, this.socket.getInputStream());
						if (readed == -1) {
							log.warn("No more data read [{} -> {}]", content.position(), total);
							return;
						}
						content.put(temp, 0, readed);
						if (content.position() == total) {
							needMoreData = false;
						}
					}
					content.flip();
					content.getInt();//version + command + len = 4
					if (len > 0) {
						byte[] bytes = new byte[len];
						content.get(bytes);
						data = new String(bytes, Charset.forName("UTF-8"));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				String response = null;
				if (command == 1) {
					response = cmdServer.normal(convert2Map(data));
				} else if (command == 2) {
					response = cmdServer.optionList();
				}
				if (response != null) {
					this.socket.getOutputStream().write(response.getBytes(Charset.forName("UTF-8")));
					this.socket.getOutputStream().flush();
				}
				log.info("Operation is done.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.netServer.socketClosed();//Connection -1
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private int read(byte[] temp, InputStream inputStream) {
			int readed = -1;
			try {
				readed = inputStream.read(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return readed;
		}
		
		private Map<String, String> convert2Map(String data) {
			Map<String, String> params = new HashMap<String, String>();
			String[] segment = data.split("[|]");
			for (String s : segment) {
				String[] kv = s.split("[=]");
				if (kv[1].equals("__NULL__")) {
					kv[1] = null;
				}
				params.put(kv[0], kv[1]);
			}
			return params;
		}
		
	}
	
	public class CmdToolsThreadFactory implements ThreadFactory {
		private AtomicInteger count = new AtomicInteger(0);
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("CmdToolsThread-" + count.getAndIncrement());
			return t;
		}
		
	}
}
