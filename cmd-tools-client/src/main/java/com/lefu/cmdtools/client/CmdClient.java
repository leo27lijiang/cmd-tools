package com.lefu.cmdtools.client;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.lefu.cmdtools.client.core.CommandFactory;
import com.lefu.cmdtools.client.core.Execution;
import com.lefu.cmdtools.client.core.ParallelExecution;
import com.lefu.cmdtools.client.core.ResponseCallback;
import com.lefu.cmdtools.client.core.SerialExceution;
import com.lefu.cmdtools.client.core.factory.NormalCommandFactory;
import com.lefu.cmdtools.client.core.factory.ServerOptionListFactory;
import com.lefu.cmdtools.client.net.ConnectionFactory;
import com.lefu.cmdtools.client.net.bio.BioConnectionFactory;
import com.lefu.cmdtools.client.net.jmx.JmxConnectionFactory;

public class CmdClient {
	public static final char SHORT_OPT_HELP = 'h';
	public static final char SHORT_OPT_SERVERS = 's';
	public static final char SHORT_OPT_LIST = 'l';
	public static final char SHORT_OPT_PARALLEL = 'p';
	public static final char SHORT_OPT_PROTOCOL = 'P';
	public static final char SHORT_OPT_PROPERTIES = 'D';
	
	private Options options;
	private CommandLine commandLine;
	
	public CmdClient() {
		options = new Options();
		options.addOption(new Option(String.valueOf(SHORT_OPT_HELP), false, "Print help information"));
		options.addOption(new Option(String.valueOf(SHORT_OPT_SERVERS), true, "Server host info, socket://host:port or service:jmx:protocol:sap, multi URI split ',' . See -P "));
		options.addOption(new Option(String.valueOf(SHORT_OPT_LIST), false, "Print server option list, must with -s , at least one host be set"));
		options.addOption(new Option(String.valueOf(SHORT_OPT_PARALLEL), true, "true: use parallel mode interact with server if have multi servers, false use serial mode, it's default mode"));
		options.addOption(new Option(String.valueOf(SHORT_OPT_PROTOCOL), true, "true: use JMX protocol false: use socket protocol, default is socket protocol"));
		options.addOption(Option.builder(String.valueOf(SHORT_OPT_PROPERTIES))
							.argName("property=value")
							.hasArgs()
							.valueSeparator()
							.desc("Use -Dkey=value to defined user properties")
							.build());
	}
	
	public void doParser(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		commandLine = parser.parse(options, args);
	}
	
	public void logic() throws Exception {
		if (commandLine == null) {
			System.err.println("CommandLine parser error");
			System.exit(1);
		}
		if (this.commandLine.hasOption(SHORT_OPT_HELP)) {
			printHelp();
			System.exit(0);
		}
		Set<String> servers = new HashSet<String>();
		boolean paralleled = false;
		boolean jmxEnable = false;
		Properties props = this.commandLine.getOptionProperties(String.valueOf(SHORT_OPT_PROPERTIES));
		if (this.commandLine.hasOption(SHORT_OPT_PROTOCOL)) {
			jmxEnable = Boolean.valueOf(this.commandLine.getOptionValue(SHORT_OPT_PROTOCOL));
		}
		if (this.commandLine.hasOption(SHORT_OPT_SERVERS)) {
			String uris = this.commandLine.getOptionValue(SHORT_OPT_SERVERS);
			for (String uri : uris.split("[,]")) {
				servers.add(uri);
			}
		}
		if (this.commandLine.hasOption(SHORT_OPT_PARALLEL)) {
			paralleled = Boolean.valueOf(this.commandLine.getOptionValue(SHORT_OPT_PARALLEL));
		}
		if (servers.isEmpty()) {
			System.err.println("No specified server found");
			System.exit(1);
		}
		ConnectionFactory connectionFactory = null;
		Execution execution = null;
		CommandFactory commandFacoty = null;
		if (jmxEnable) {
			connectionFactory = new JmxConnectionFactory();
		} else {
			connectionFactory = new BioConnectionFactory();
		}
		if (this.commandLine.hasOption(SHORT_OPT_LIST)) {
			commandFacoty = new ServerOptionListFactory();
		} else {
			commandFacoty = new NormalCommandFactory();
		}
		if (paralleled) {
			execution = new ParallelExecution(connectionFactory);
		} else {
			execution = new SerialExceution(connectionFactory);
		}
		//Use system out print the response
		execution.work(commandFacoty, servers, props, new ResponseCallback(){

			@Override
			public void onResponse(String response) {
				System.out.println(response);
			}

			@Override
			public void onException(Throwable cause) {
				cause.printStackTrace();
			}
			
		});
	}
	
	private void printHelp() {
		System.out.println("Usage:");
		for(Option opt : this.options.getOptions()) {
			System.out.println("-" + opt.getOpt() + " " + opt.getDescription());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CmdClient cmdClient = new CmdClient();
		try {
			cmdClient.doParser(args);
			cmdClient.logic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
