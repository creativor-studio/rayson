
package org.rayson.server.transport;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.rayson.server.Container;
import org.rayson.server.Manager;
import org.rayson.server.exception.InternalServerError;
import org.rayson.share.transport.Connection;
import org.rayson.share.transport.NioChannel;
import org.rayson.share.transport.NioSelector;
import org.rayson.share.transport.NioSelectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link NioSelectorBase} of server side.
 * 
 * @author Nick Zhang
 *
 */
public class ServerSelector extends NioSelectorBase implements Manager, NioSelector {
	private static final int MAX_CONNECTED_CONNECTIONS = 10000;

	private static Logger LOGGER = LoggerFactory.getLogger(ServerSelector.class);

	private boolean running = false;
	private final ServerSocketChannel socketChannel;
	private Thread thread;

	private final Container container;

	/**
	 * @param name Name of this new selector.
	 * @param selector Underling channel selector.
	 * @param socketChannel Underling socket channel.
	 * @param container Server container instance .
	 */
	public ServerSelector(final String name, final Selector selector, final ServerSocketChannel socketChannel, final Container container) {
		super(name, selector);
		this.socketChannel = socketChannel;
		this.container = container;
	}

	@Override
	protected void preSelect() {
		// Do nothing.
	}

	private void closeCurrentSocket(final SelectionKey key) {
		if (key == null || !key.isReadable())
			return;
		try {
			Connection c = (Connection) key.attachment();
			if (c != null) {
				c.close();
			}
		} catch (Throwable e) {
			// Ignore it.
			LOGGER.error("Failed to close connection", e);
		}

	}

	@Override
	protected void doAccept(final SelectionKey key) {
		final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
		// NioConnection connection = null;

		try {
			int connectedCount = container.getConnectionManager().count();
			if (connectedCount >= MAX_CONNECTED_CONNECTIONS) {
				LOGGER.warn("Max connected connections achieved, so we do not accept new connection");
				channel.close();
			}
			final SocketChannel acceptedChannel = channel.accept();
			acceptedChannel.configureBlocking(false);
			// Make socket keep alive.
			acceptedChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

			// register read events for the new connection.
			final SelectionKey newKey = acceptedChannel.register(selector, SelectionKey.OP_READ);

			final NioChannel nioChannel;
			nioChannel = this.container.getServer().createChannel(newKey, acceptedChannel);

			final ServerConnection connection = container.getConnectionManager().newConnection(nioChannel);
			newKey.attach(connection);
			container.getConnectionManager().put(connection);
			LOGGER.debug("new connection: " + connection + " accepted.");

		} catch (final Throwable e) {
			LOGGER.error("Accept new connection error", e);
		}
	}

	@Override
	protected void doConnect(SelectionKey key) {
		// Do nothing.

	}

	@Override
	protected void doRead(final SelectionKey key) {

		final ServerConnection connection = (ServerConnection) key.attachment();
		try {
			connection.asyncRead();
		} catch (final Throwable e) {
			if (!(e instanceof IOException))
				LOGGER.error("Async read from connection got error", e);
			try {
				// First cancel the read event.
				connection.getChannel().unInterestRead();
				container.getConnectionManager().recycle(connection.getId());
			} catch (Throwable e1) {
				// Ignore it.
			}
		}

	}

	@Override
	protected void doWrite(final SelectionKey key) {
		final ServerConnection connection = (ServerConnection) key.attachment();
		try {
			connection.asyncWrite();
		} catch (final Throwable e) {
			if (!(e instanceof IOException))
				LOGGER.error("Async read from connection got error", e);
			try {
				// First cancel the write event.
				connection.getChannel().unInterestWrite();
				container.getConnectionManager().recycle(connection.getId());
			} catch (Throwable e1) {
				// Ignore it.
			}
		}
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public void run() {
		SelectionKey sk = null;
		while (running) {

			try {

				doSelect();

			} catch (final OutOfMemoryError e) {
				try {

					LOGGER.error(this.getName() + " got OOME error:", e);
					// Close current key's socket channel.
					closeCurrentSocket(sk);
					// We should do a garbage collection first.
					LOGGER.info("Do garbage collection.");
					System.gc();
					LOGGER.info("Trying to interrupt the connection manager.");
					// then remove all time out connections.
					container.getConnectionManager().interrupt();

				} catch (Throwable e1) {
					// Ignore this exception.
				}

			} catch (final ClosedSelectorException e) {
				if (running)
					LOGGER.error(getName() + " seems closed", e);
			} catch (final Throwable e) {
				if (running)
					LOGGER.error(getName() + " got runtime error", e);
			}

		}
	}

	@Override
	public void start() throws InternalServerError {
		try {
			this.socketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			throw new InternalServerError("Failed to register selector", e);
		}

		// Set to true first.
		this.running = true;

		this.thread = new Thread(this, this.getName());
		this.thread.setDaemon(false);
		this.thread.start();

	}

	@Override
	public void stop() throws InternalServerError {

		if (!this.running)
			return;

		try {
			this.selector.close();
			// socket channel is not opened here, so it should not close it
			// here.
			// this.socketChannel.close();
		} catch (IOException e) {
			throw new InternalServerError("Failed to close underling selector", e);
		}
		this.thread.interrupt();

		this.running = false;
	}
}
