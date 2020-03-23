/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.net.InetAddress;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.rayson.api.Protocol;
import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.server.ServerConfig.ConfigKey;
import org.rayson.api.server.ServerDiscovery;
import org.rayson.client.Rayson;
import org.rayson.server.RaysonServer;
import org.rayson.server.ServerConfigImpl;
import org.rayson.server.exception.InternalServerError;
import org.rayson.share.util.ReflectionTool;

/**
 * An base class used to test basic whole work flow of Rayson。
 * 
 * @param <P> API protocol interface.
 *
 */
public abstract class BaseFlowTest<P extends Protocol> {
	@TestStrategy
	private class FlowConfigRule implements TestRule {

		private Description description;
		private TestStrategy strategy = FlowConfigRule.class.getAnnotation(TestStrategy.class);

		/**
		 * Override to tear down your specific external resource.
		 * 
		 * @throws InternalServerError
		 */
		protected void after() throws InternalServerError {
			server.stop();
			proxy = null;
		}

		@Override
		public Statement apply(Statement base, Description description) {
			this.description = description;

			for (Annotation anno : description.getAnnotations()) {
				if (anno.annotationType().equals(TestStrategy.class)) {
					strategy = TestStrategy.class.cast(anno);
					break;
				}
			}

			return statement(base);
		}

		/**
		 * Override to set up your specific external resource.
		 *
		 * @throws Throwable if setup fails (which will disable {@code after}
		 */
		protected void before() throws Throwable {
			// 1. Start server.
			BaseFlowTest.this.server = initServer(strategy);
			// 2. Initialize client.
			BaseFlowTest.this.proxy = initProxy(strategy);
		}

		private Statement statement(final Statement base) {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					try {
						before();
						base.evaluate();
					} finally {
						after();
					}
				}
			};
		}
	}

	private static final int PORT_NUMBER = 7777;

	private final Class<P> protocol;

	private P proxy;
	@Rule
	public FlowConfigRule rule = new FlowConfigRule();
	private RaysonServer server;

	protected BaseFlowTest() {
		protocol = (Class<P>) ((ParameterizedType) BaseFlowTest.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Initialize client side default RPC proxy.<br>
	 * 
	 * @param strategy
	 * @return
	 */
	protected P createDefaultClient(TestStrategy strategy) throws Exception {
		ProxyConfig config = new ProxyConfig();

		Class<? extends HttpClientFilter> clientFilter = strategy.clientFilter();

		if (clientFilter != HttpClientFilter.class) {
			config.addHttpFilter(clientFilter);
		}

		RaysonServerAddress serverAddr = new RaysonServerAddress(InetAddress.getLocalHost(), PORT_NUMBER, strategy.usingSsl());
		return Rayson.createProxy(serverAddr, protocol, config);
	}

	/**
	 * Create and start an default server.
	 * 
	 * @param strategy
	 * @return
	 * @throws InternalServerError
	 */
	protected RaysonServer createDefaultServer(TestStrategy strategy) throws InternalServerError {
		ServerConfigImpl serverConfig = ServerConfigImpl.load(PORT_NUMBER);
		serverConfig.setValue(ConfigKey.SSL_ENABLED, strategy.usingSsl());
		server = new RaysonServer(PORT_NUMBER, serverConfig);
		server.start();
		return server;
	}

	/**
	 * Create protocol service instance.<br>
	 * 
	 * By default , it return <code>null</code>, child class should implement
	 * it.
	 * 
	 * @return
	 */
	protected P createService() {
		return null;
	}

	/**
	 * @return The client side proxy of this testing.
	 */
	protected final P getProxy() {
		return proxy;
	}

	/**
	 * @return The rayson server of this testing.
	 */
	protected final RaysonServer getServer() {
		return server;
	}

	/**
	 * 
	 * @param strategy
	 * @return
	 * @throws @throws Exception
	 */
	private P initProxy(TestStrategy strategy) throws Exception {

		Class<? extends ProxyCreator> creatorClass = strategy.proxyCreator();
		if (creatorClass == ProxyCreator.class) {
			return createDefaultClient(strategy);
		} else {
			ProxyCreator creator = ReflectionTool.newInstance(creatorClass);
			return (P) creator.create(strategy, this);
		}
	}

	private RaysonServer initServer(TestStrategy strategy) throws Exception {

		Class<? extends ServerCreator> creatorClass = strategy.serverCreator();
		RaysonServer server;
		if (creatorClass == ServerCreator.class) {
			server = createDefaultServer(strategy);
		} else {
			ServerCreator creator = ReflectionTool.newInstance(creatorClass);
			server = creator.create(strategy, this);
		}

		if (protocol != ServerDiscovery.class) {
			P service = createService();
			server.registerService(service);
		}

		return server;
	}
}