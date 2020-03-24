/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.server.ServerConfig.ConfigKey;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.client.ClientSystem;
import org.rayson.client.Rayson;
import org.rayson.server.RaysonServer;
import org.rayson.server.ServerConfigImpl;
import org.rayson.server.exception.InternalServerError;
import org.rayson.tools.console.ConsoleConfig;
import org.rayson.tools.console.ConsoleException;
import org.rayson.tools.console.MessageType;
import org.rayson.tools.console.RConsole;
import org.rayson.tools.profiler.rpc.ProfilerEntity;
import org.rayson.tools.profiler.rpc.ProfilerProtocol;

/**
 * An {@link RConsole} used to profile Rayson.
 * 
 * @author creativor
 */
@ConsoleConfig(arugments = { UrlArgumentDef.class }, options = { ThreadCountDef.class, CallCountDef.class, ContentTypeDef.class, DebugDef.class,
		SslOptionDef.class, VerifyDef.class, StandaloneServerDef.class })
public class Console extends RConsole<ProfilerParameters> {

	private static class ProfilerImpl implements ProfilerProtocol {

		@Override
		public ProfilerEntity echo(ProfilerEntity req) throws RpcException {
			return req;
		}

	}

	private class Reporter {

		private int callCount;
		private final AtomicLong succeedCall;

		private int threadCount;
		/**
		 * In milliseconds.
		 */
		private final AtomicLong totallyTime;

		public Reporter(int threadCount, int callCount) {
			this.threadCount = threadCount;
			this.callCount = callCount;
			totallyTime = new AtomicLong(0);
			succeedCall = new AtomicLong();
		}

		/**
		 * @return the succeedCall
		 */
		public long getSucceedCall() {
			return succeedCall.get();
		}

		/**
		 * 
		 * @param takeTime One call take time. In nano-seconds.
		 */
		void increaseCall(long takeTime) {
			succeedCall.incrementAndGet();
			totallyTime.addAndGet(takeTime);
		}

		public void report() {
			StringBuffer sb = new StringBuffer();
			sb.append(System.lineSeparator());
			sb.append("==========Testing report=======");
			sb.append(System.lineSeparator());
			sb.append("Using ssl: " + getParameters().isUsingSsl());
			sb.append(System.lineSeparator());
			sb.append("Verify result : " + getParameters().isVerify());
			sb.append(System.lineSeparator());
			sb.append("Thread count: " + threadCount);
			sb.append(System.lineSeparator());
			sb.append("Thread count: " + threadCount);
			sb.append(System.lineSeparator());
			sb.append("Call each thread count: " + callCount);
			sb.append(System.lineSeparator());
			sb.append("HTTP content type: " + getParameters().getContentType());
			sb.append(System.lineSeparator());
			sb.append("Successful call count: " + succeedCall.get());
			sb.append(System.lineSeparator());
			if (succeedCall.get() > 0) {
				double eachMilliSeconds = totallyTime.get() / 1E6 / succeedCall.get();
				sb.append("Each call take time: " + eachMilliSeconds + " milli-seconds");
				sb.append(System.lineSeparator());
			}

			printMsg(sb.toString());

		}

	}

	private class Runner extends Thread {

		private int index;
		private ProfilerProtocol proxy;

		/**
		 * @param i
		 */
		public Runner(int i) {
			this.index = i;
			setName("Test Runner " + index);
		}

		/**
		 * @throws RpcException
		 * @throws IOException
		 * @throws ConsoleException
		 * 
		 */
		private void doOneCall() throws IOException, RpcException, ConsoleException {
			ProfilerEntity resp = proxy.echo(TEST_ENTITY);
			if (getParameters().isVerify()) {
				if (!Objects.equals(resp, TEST_ENTITY))
					throw new ConsoleException("Response do not match the request");
			}
			return;
		}

		@Override
		public void run() {
			try {
				proxy = Rayson.createProxy(getParameters().getServerAddress(), PROTOCOL);

				long startTime;
				long endTime;
				for (int i = 0; i < getParameters().getCallCount(); i++) {
					startTime = System.nanoTime();
					try {
						doOneCall();
						endTime = System.nanoTime();
						reporter.increaseCall(endTime - startTime);
						if (getParameters().isDebug()) {
							printMsg("Seccessfull call count :" + reporter.getSucceedCall() + System.lineSeparator());
						}
					} catch (Throwable e) {
						printMsg("Thread " + getName() + " failed to call rpc: " + e.getMessage());

					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				printMsg("Thread " + getName() + " failed: " + e1.getMessage());
			} finally {

				// Notify that one thread is quit.
				countDownLatch.countDown();
			}
		}

	}

	private static final int BUILTIN_SERVER_PORT = 6666;

	private static final Class<ProfilerProtocol> PROTOCOL = ProfilerProtocol.class;

	private static final String SCRIPT_NAME = "rprofiler";

	private static final ProfilerEntity TEST_ENTITY;
	static {
		byte[] arrayField = new byte[] { 1, 2, 3, 4, 5 };
		TEST_ENTITY = ProfilerEntity.builder().withIntField(108).withStrField("hello world").withArrayField(arrayField).build();
	}

	public static void main(final String[] args) {
		Console console;
		try {
			console = new Console();
		} catch (final ConsoleException e) {
			System.err.println("Initialize console error:" + e.getMessage());
			return;
		}
		try {
			console.run(args);
		} catch (final ConsoleException | InterruptedException e) {
			console.printMsg(MessageType.ERROR, e.getMessage());
		}
	}

	private CountDownLatch countDownLatch;

	private Reporter reporter;

	/**
	 * @throws ConsoleException
	 * 
	 */
	Console() throws ConsoleException {
		super();
	}

	@Override
	protected void execute(final ProfilerParameters parameters) throws ConsoleException, InterruptedException {
		printMsgLn("Running test using paramters: " + parameters.toString() + " ...");

		RaysonServerAddress serverAddress = getParameters().getServerAddress();

		if (serverAddress == null) {
			printMsgLn("No server address specified, so we start an built in server to profile.");
			try {
				setupBuiltinServer();
			} catch (Exception e) {
				throw new ConsoleException("Failed to start built in server", e);
			}
		}

		if (getParameters().isStandaloneServer()) {
			printMsg("Runing in standalone server mode ...");
			return;
		}

		int threadCount = getParameters().getThreadCount();
		// 1. Setup profiling environment.

		// Setup content type.
		ClientSystem.getSystem().getConfig().setValue(org.rayson.client.ClientConfig.ConfigKey.HTTP_CONTENT_TYPE, getParameters().getContentType().typeName());

		countDownLatch = new CountDownLatch(threadCount);
		reporter = new Reporter(threadCount, getParameters().getCallCount());

		// 2. Startup the runners.
		Runner runner;
		for (int i = 0; i < threadCount; i++) {
			runner = new Runner(i);
			runner.start();
		}

		// 3. Wait for all runner finished.

		while (!countDownLatch.await(10000, TimeUnit.MILLISECONDS)) {
			this.printMsg(".");
		}

		// 4. Report the result.
		reporter.report();

		// 5. Exit.
		System.exit(0);
	}

	private void setupBuiltinServer()
			throws InternalServerError, IllegalArgumentException, UnknownHostException, DuplicateServiceException, ServiceException, InvalidApiException {
		ServerConfigImpl config = ServerConfigImpl.load(BUILTIN_SERVER_PORT);
		boolean usingSsl = getParameters().isUsingSsl();
		config.setValue(ConfigKey.SSL_ENABLED, usingSsl);
		RaysonServer builtinServer = new RaysonServer(BUILTIN_SERVER_PORT, config);
		ProfilerImpl profilerImpl = new ProfilerImpl();
		builtinServer.registerService(profilerImpl);
		builtinServer.start();
		getParameters().setServerAddress(new RaysonServerAddress("127.0.0.1", BUILTIN_SERVER_PORT, usingSsl));

	}

	@Override
	protected String usage() {
		return SCRIPT_NAME
				+ "[-sa (or --standalone for standalone server mode)] [-e] [-n thread_count] [-c call_count_each_thread] [-ct http_content_type] [-v] [serverIp:serverPort]";
	}
}