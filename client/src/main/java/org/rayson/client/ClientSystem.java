/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpMessage;
import org.rayson.client.ClientConfig.ConfigKey;
import org.rayson.client.rpc.RpcManager;
import org.rayson.client.transport.ClientSelector;
import org.rayson.client.transport.ConnectionPool;
import org.rayson.client.transport.SslClientSelector;
import org.rayson.share.config.ConfigBase;
import org.rayson.share.http.HttpHeaderImpl;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.rayson.share.serial.ContentCoder;
import org.rayson.share.serial.ContentCoderFactory;
import org.rayson.share.transport.NioChannel;
import org.rayson.share.transport.SslChannel;
import org.rayson.share.transport.SslNioSelector;
import org.slf4j.Logger;

/**
 * Rayson Client side system. Used as a container of all the key modules in
 * client side.
 * 
 * @author creativor
 */
public class ClientSystem {

	private static ConnectionPool connectionPool;

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger("RaysonClient");

	private static ClientSystem SINGLETON = new ClientSystem();

	/**
	 * @return Singleton client system instance.
	 */
	public static ClientSystem getSystem() {
		return SINGLETON;
	}

	private ClientConfig config;

	private ContentCoder contentCoder;
	private HttpHeader contentTypeHeader;
	private boolean inited = false;

	private SslClientSelector nioSelector;

	private RpcManager rpcManager;

	private SSLContext sslContext;

	// Forbidden.
	private ClientSystem() {
		// First of all, load configuration.
		config = ClientConfig.load();
	}

	/**
	 * Create a new {@link NioChannel} out of this client system.
	 * 
	 * @param socketChannel Underling socket channel of the new
	 *            {@link NioChannel} .
	 * @param usingSsl Whether using SSL.
	 * @return New created NIO channel.
	 * @throws IllegalArgumentException If the channel is <code>null</code>.
	 */
	public NioChannel createChannel(@NotNull SocketChannel socketChannel, boolean usingSsl) {
		if (socketChannel == null)
			throw new IllegalArgumentException("socketChannel should not be null");
		if (usingSsl) {
			lazyInitSslContext();
			SSLEngine engine = sslContext.createSSLEngine();
			engine.setUseClientMode(true);
			return new SslChannel(socketChannel, (SslNioSelector) getNioSelector(), engine);
		} else {
			return new NioChannel(socketChannel);
		}
	}

	private void doInit() {
		String contentType = config.getValue(ConfigKey.HTTP_CONTENT_TYPE);
		contentCoder = ContentCoderFactory.get(contentType);
		rpcManager = new RpcManager();
		try {
			// Always using SSL verison selector.
			nioSelector = new SslClientSelector(Selector.open(), "Rayson Client Selector");
			new Thread(nioSelector, nioSelector.getName()).start();
		} catch (final IOException e) {
			throw new RuntimeException("Failed to initialize NIO selector", e);
		}

		connectionPool = new ConnectionPool(this);
		connectionPool.initialize();

		contentTypeHeader = new HttpHeaderImpl(HttpConstants.CONTENT_TYPE_HEADER_NAME, contentType + "; charset=" + HttpMessage.CHARSET.name());
	}

	/**
	 * @return The client side configuration.
	 */
	public synchronized ClientConfig getConfig() {
		return config;
	}

	/**
	 * @return Connection pool instance.
	 */
	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	/**
	 * @return Content coder instance.
	 */
	public ContentCoder getContentCoder() {
		return contentCoder;
	}

	/**
	 * @return Content type HTTP header according to this client system's
	 *         settings.
	 */
	public HttpHeader getContentTypeHeader() {
		return contentTypeHeader;
	}

	/**
	 * @return Logger instance of this client system.
	 */
	public Logger getLogger() {
		return LOGGER;
	}

	/**
	 * @return NIO selector instance.
	 */
	public ClientSelector getNioSelector() {
		return nioSelector;
	}

	/**
	 * @return the RPC manager instance of this client system.
	 */
	public RpcManager getRpcManager() {
		return rpcManager;
	}

	private SSLContext initSslContext() throws Exception {

		final SSLContext instance = SSLContext.getInstance("SSL");

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		KeyStore trustStore = KeyStore.getInstance("JKS");

		String trustStoreFile = config.getValue(ConfigKey.SSL_TRUSTSTORE_FILE);

		String filePath = ClientConfig.BASE_DIRECTORY + File.separator + trustStoreFile;

		URL url = ConfigBase.searchConfigFile(filePath);

		if (url == null) {
			throw new Exception("SSL key store file " + filePath + " not found");
		}

		String password = config.getValue(ConfigKey.SSL_TRUSTSTORE_PASSWORD);
		try (InputStream in = url.openStream()) {
			trustStore.load(in, password.toCharArray());
		}
		tmf.init(trustStore);
		instance.init(null, tmf.getTrustManagers(), null);
		return instance;

	}

	/**
	 * Lazy initialize the system.
	 */
	synchronized void lazyInit() {
		if (inited) {
			return;
		}
		doInit();
		inited = true;
	}

	private synchronized void lazyInitSslContext() {
		if (sslContext == null) {
			try {
				sslContext = initSslContext();
			} catch (Exception e) {
				throw new RuntimeException("Failed to initialize SSL context", e);
			}
		}
	}

	synchronized void setConfig(ClientConfig config) {
		if (config == null)
			throw new IllegalArgumentException("config should not be null");

		this.config = config;
		sslContext = null;
	}
}