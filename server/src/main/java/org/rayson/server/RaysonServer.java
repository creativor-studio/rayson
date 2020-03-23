
package org.rayson.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.protocol.info.ServerInfo;
import org.rayson.api.protocol.info.ServerInfo.Builder;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.Server;
import org.rayson.api.server.ServerConfig;
import org.rayson.api.server.ServerConfig.ConfigKey;
import org.rayson.api.server.ServiceFilter;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.server.exception.InternalServerError;
import org.rayson.server.transport.ServerSelector;
import org.rayson.server.transport.SslServerSelector;
import org.rayson.share.config.ConfigBase;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.rayson.share.transport.NioChannel;
import org.rayson.share.transport.NioSelector;
import org.rayson.share.transport.SslChannel;
import org.rayson.share.transport.SslNioSelector;
import org.slf4j.Logger;

/**
 * Server implementation of Rayson system.
 * 
 * @author Nick Zhang
 *
 */
public class RaysonServer implements Server {

	private static final String DEFAULT_KEY_MANAGER_ALGORITHM = "SunX509";

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(RaysonServer.class);

	private static final int SOCKET_BACKLOG = 300;

	private static final int SOCKET_TIMEOUT = 1000 * 30;

	/**
	 * Version of this rayson server implementation.
	 */
	private static final int VERSION = 1;
	private final ServerConfig config;
	private final Container container;

	private int portNumber;

	private ServerSelector selector;
	private ServerSocketChannel socketChannel;

	private SSLContext sslContext;

	private boolean usingSsl;

	/**
	 * @param portNumber Port number the new server used.
	 */
	public RaysonServer(final int portNumber) {
		this(portNumber, ServerConfigImpl.load(portNumber));
	}

	/**
	 * @param portNumber Port number the new server used.
	 * @param config Configuration setting of the new server.
	 */
	public RaysonServer(final int portNumber, final ServerConfig config) {
		super();
		this.portNumber = portNumber;
		this.config = config;
		this.container = new Container(this);
		usingSsl = this.config.getAsBoolean(ConfigKey.SSL_ENABLED);

	}

	@Override
	public void addHttpFilter(@NotNull Class<? extends HttpServerFilter> type) throws ServiceException {
		this.container.getServiceManager().addHttpFilter(type);
	}

	@Override
	public void addServiceFilter(@NotNull Class<? extends ServiceFilter> type) throws ServiceException {
		this.container.getServiceManager().addServiceFilter(type);
	}

	/**
	 * Create a new {@link NioChannel} out of this server.
	 * 
	 * @param newKey New selection key from new accepted socket channel.
	 * @param acceptedChannel New accepted socket channel.
	 * @return An new NIO channel out of this server.
	 */
	public NioChannel createChannel(SelectionKey newKey, SocketChannel acceptedChannel) {
		if (usingSsl) {
			SSLEngine engine = this.sslContext.createSSLEngine();
			engine.setUseClientMode(false);
			return new SslChannel(newKey, acceptedChannel, (SslNioSelector) selector, engine);
		} else {
			return new NioChannel(acceptedChannel, newKey);
		}
	}

	@Override
	public ServerConfig getConfig() {
		return config;
	}

	/**
	 * @return Modules container of this server.
	 */
	public Container getContainer() {
		return container;
	}

	@Override
	public ServerInfo getInfo() {
		Builder b = ServerInfo.builder();
		b.withPortNumber(portNumber).withSslEnabled(usingSsl).withVersion(VERSION);
		return b.build();
	}

	@Override
	public String getName() {

		return "Rayson server" + (usingSsl ? "(ssl)" : "") + " on " + this.portNumber;
	}

	@Override
	public int getPortNumber() {
		return portNumber;
	}

	NioSelector getSelector() {
		return selector;
	}

	private void initSsl() throws InternalServerError {
		try {

			KeyStore keyStore;

			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

			String keyStoreFile = this.config.getValue(ConfigKey.SSL_KEYSTORE_FILE);
			String filePath = ServerConfigImpl.BASE_DIRECTORY + File.separator + keyStoreFile;

			URL url = ConfigBase.searchConfigFile(filePath);

			if (url == null) {
				throw new InternalServerError("SSL key store file " + filePath + " not found");
			}

			String password = this.config.getValue(ConfigKey.SSL_KEYSTORE_PASSWORD);
			try (InputStream in = url.openStream()) {
				keyStore.load(in, password.toCharArray());
			}

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(DEFAULT_KEY_MANAGER_ALGORITHM);

			String keyPwd = this.config.getValue(ConfigKey.SSL_KEY_PASSWORD);
			if (keyPwd == null || keyPwd.isEmpty())
				keyPwd = password;

			kmf.init(keyStore, keyPwd.toCharArray());

			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(kmf.getKeyManagers(), null, null);

		} catch (Exception e) {
			throw new InternalServerError("Failed to init ssl context", e);
		}
	}

	@Override
	public void registerService(final Protocol service) throws DuplicateServiceException, ServiceException, InvalidApiException, IllegalArgumentException {
		this.container.getServiceManager().registerService(service);
	}

	/**
	 * Start this server.
	 * 
	 * @throws InternalServerError If the starting process got error.
	 */
	public void start() throws InternalServerError {
		LOGGER.info(getName() + " starting ...");

		try {
			socketChannel = ServerSocketChannel.open();

			socketChannel.configureBlocking(false);
			socketChannel.socket().setSoTimeout(SOCKET_TIMEOUT);
			final InetSocketAddress socketAddress = new InetSocketAddress(this.portNumber);
			socketChannel.socket().bind(socketAddress, SOCKET_BACKLOG);
			this.portNumber = socketAddress.getPort();
			if (usingSsl) {
				initSsl();
			}

			this.container.start();
			if (usingSsl) {
				this.selector = new SslServerSelector(getName(), Selector.open(), socketChannel, container);

			} else {
				this.selector = new ServerSelector(getName(), Selector.open(), socketChannel, container);
			}
			this.selector.start();
		} catch (IOException e1) {
			throw new InternalServerError("Failed to start server", e1);
		}

	}

	/**
	 * Stop this server.
	 * 
	 * @throws InternalServerError If the stopping process got error.
	 */
	public void stop() throws InternalServerError {
		LOGGER.info(getName() + " stopping ...");

		// 1. Stop selector first.
		if (this.selector != null)
			this.selector.stop();
		// 2. Close the socket channel here, cause it is opened here.
		if (socketChannel != null) {
			try {
				socketChannel.close();
			} catch (IOException e) {
				throw new InternalServerError("Failed to close underling  socket channel", e);
			}
		}
		// 3. Stop the container.
		this.container.stop();
	}

	@Override
	public String toString() {
		return getName();
	}
}