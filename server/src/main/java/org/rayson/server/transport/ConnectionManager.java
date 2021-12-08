
package org.rayson.server.transport;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.api.server.ServerConfig.ConfigKey;
import org.rayson.server.Container;
import org.rayson.server.Manager;
import org.rayson.server.exception.InternalServerError;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.rayson.share.transport.NioChannel;
import org.slf4j.Logger;

/**
 * An manager role used to manage {@link ServerConnection}s.
 * 
 * @author Nick Zhang
 *
 */
public class ConnectionManager implements Manager, Runnable {
	private static Logger LOGGER = RaysonLoggerFactory.getLogger(ServerSelector.class);
	private final AtomicLong connectionId;
	private final Container container;
	private final ConcurrentHashMap<Long, ServerConnection> connections;
	private final LinkedBlockingQueue<ServerConnection> recycleConnections;
	/**
	 * Check connections interval, in milli-seconds.
	 */
	private final int checkInterval;
	private boolean running;

	private Thread thread;

	/**
	 * Construct a new connection manager with it's container.
	 * 
	 * @param container Container which hold the new connection manager.
	 */
	public ConnectionManager(final Container container) {
		this.container = container;
		connectionId = new AtomicLong();
		connections = new ConcurrentHashMap<>();
		recycleConnections = new LinkedBlockingQueue<ServerConnection>();
		int timeout = container.getServer().getConfig().getAsNumber(ConfigKey.NETWORK_KEEPALIVE_TIMEOUT).intValue() * 1000;
		checkInterval = timeout / 2;
	}

	private void checkTimeout() {
		for (final Iterator<Entry<Long, ServerConnection>> iterator = connections.entrySet().iterator(); iterator.hasNext();) {
			final Entry<Long, ServerConnection> entry = iterator.next();
			if (entry.getValue().isTimeout()) {
				iterator.remove();
				recycleConnections.add(entry.getValue());
			}
		}
	}

	private void doClose(final ServerConnection conn) {

		LOGGER.debug("Closing " + conn + " ...");
		try {
			conn.close();
		} catch (final Throwable e) {
			LOGGER.error("Failed to close " + conn, e);
			// close failed, add to pool again.
			put(conn);
		}

	}

	/**
	 * @return All connections count managed by this manager.
	 */
	public int count() {
		return connections.size();
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public String getName() {
		return "Connection Manager of " + container.getServer().getName();
	}

	/**
	 * Create a new connection instance out of gien {@link NioChannel}.
	 * 
	 * @param nioChannel Source {@link NioChannel}.
	 * @return New created connection.
	 */
	public ServerConnection newConnection(final NioChannel nioChannel) {
		return new ServerConnection(connectionId.getAndIncrement(), nioChannel, container);
	}

	/**
	 * Put a connection into this manager.
	 * 
	 * @param connection Connection to be added.
	 */
	void put(final ServerConnection connection) {
		connections.put(connection.getId(), connection);

	}

	/**
	 * Recycle an connection. The associated connection will be closed
	 * asynchronously by this manager.If associated connection is not managed by
	 * this manager, it will do nothing.
	 * 
	 * @param connectionId Connection id to be removed.
	 */
	void recycle(final long connectionId) {
		final ServerConnection conn = connections.remove(connectionId);
		if (conn == null)
			return;
		recycleConnections.add(conn);
	}

	@Override
	public void run() {

		OUTER: while (running) {
			// Reset the interrupted status.
			Thread.interrupted();
			checkTimeout();
			INNER: while (running) {
				try {
					final ServerConnection conn = recycleConnections.poll(checkInterval, TimeUnit.MILLISECONDS);
					if (conn == null)
						checkTimeout();
					else {
						doClose(conn);
					}
				} catch (final InterruptedException e) {
					if (running)
						LOGGER.error(getName() + " is interrupted", e);
					continue OUTER;
				} catch (Throwable e) {
					if (running)
						LOGGER.error(getName() + " got run time error", e);
					// Ignore it.
					continue INNER;
				}
			} // INNER
		} // OUTER
	}

	@Override
	public void start() throws InternalServerError {
		LOGGER.info(getName() + " starting ...");

		running = true;

		thread = new Thread(this, getName());
		thread.start();

	}

	/**
	 * Interrupt the underling thread of this connection manager. This will may
	 * this thread restart the {@link #run()} process.
	 */
	void interrupt() {
		thread.interrupt();
	}

	@Override
	public void stop() {
		if (!running)
			return;
		LOGGER.info(getName() + " stopping ...");
		thread.interrupt();
		running = false;
	}

}