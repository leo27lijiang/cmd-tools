package com.lefu.cmdtools.client.test;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.lefu.cmdtools.client.core.CommandFactory;
import com.lefu.cmdtools.client.core.Execution;
import com.lefu.cmdtools.client.core.ResponseCallback;
import com.lefu.cmdtools.client.core.SerialExceution;
import com.lefu.cmdtools.client.core.factory.NormalCommandFactory;
import com.lefu.cmdtools.client.net.bio.BioConnectionFactory;

import junit.framework.TestCase;

public class SerialApiTest extends TestCase {
	private Execution execution;
	private CommandFactory factory;
	
	@Override
	public void setUp() {
		execution = new SerialExceution(new BioConnectionFactory());
		factory = new NormalCommandFactory();
	}
	
	@Test
	public void testApi() {
		Set<String> servers = new HashSet<String>();
		servers.add("socket://192.168.13.64:13400");
		servers.add("socket://192.168.13.64:13401");
		Properties props = new Properties();
		props.put("SaySomething", "HOHO");
		try {
			execution.work(factory, servers, props, new ResponseCallback(){

				@Override
				public void onResponse(String response) {
					System.out.println(response);
				}

				@Override
				public void onException(Throwable cause) {
					cause.printStackTrace();
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void tearDown() {
		
	}
	
}
