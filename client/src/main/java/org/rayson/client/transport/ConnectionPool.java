/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.transport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.ThreadSafe;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.client.ClientConfig.ConfigKey;
import org.rayson.client.ClientSystem;
import org.rayson.client.Rayson;
import org.slf4j.Logger;

/**
 * An {@link ClientConnection} pool.
 * 
 * @author creativor
 */
public class ConnectionPool {

	/**
	 * Monitor thread for this pool.
	 * 
	 * @author creativor
	 */
	private class Monitor extends Thread {
		private final LinkedBlockingQueue<ClientConnection> closeTasks;

		private Monitor() {
			setName("Connection pool monitor thread of Rayson Client");
			setDaemon(true);
			closeTasks = new LinkedBlockingQueue<>();
		}

		private void addTask(@NotNull final ClientConnection conn) {
			closeTasks.add(conn);
		}

		private void doCheck() {
			for (final Iterator<Entry<Long, ClientConnection>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				final Entry<Long, ClientConnection> entry = iterator.next();
				synchronized (ConnectionPool.this) {
					if (entry.getValue().isTimeout()) {
						iterator.remove();
						addTask(entry.getValue());
					}
				}
			}
		}

		private void doClose(final ClientConnection conn) {
			LOG.info("Closing " + conn + " ...");
			try {
				conn.close();
			} catch (final Throwable e) {
				LOG.error("Failed to close " + conn, e);
				// close failed, add to pool again.
				put(conn);
			}
		}

		@Override
		public void run() {

			LOG.info(getName() + " starting ...");
			ClientConnection conn;
			while (true) {
				try {
					conn = closeTasks.poll(checkInterval, TimeUnit.MILLISECONDS);
					if (conn == null)
						doCheck();
					else {
						doClose(conn);
					}
				} catch (final InterruptedException e) {
					LOG.error(getName() + " quit!", e);
					return;
				}
			}
		}
	}

	private final AtomicLong uid;
	private final Monitor monitor;
	private static Logger LOG = ClientSystem.getSystem().getLogger();
	@ThreadSafe
	private final Map<Long, ClientConnection> map;
	private ClientSystem system;
	/**
	 * Check connections interval, in milli-seconds.
	 */
	private final int checkInterval;

	/**
	 * Construct a new connection pool and initialize the fields.
	 * 
	 * @param system Client system instance the new connection pool belongs to.
	 */
	public ConnectionPool(ClientSystem system) {
		this.system = system;
		uid = new AtomicLong();
		monitor = new Monitor();
		map = Collections.synchronizedMap(new HashMap<Long, ClientConnection>());
		int timeout = system.getConfig().getAsNumber(ConfigKey.NETWORK_KEEPALIVE_TIMEOUT).intValue() * 1000;
		this.checkInterval = timeout / 2;
	}

	/**
	 * Create a new connection.Notes, the new connect still not be managed by
	 * this pool.
	 * 
	 * @param serverAddr Remote server socket address.
	 * @return New created connection.
	 * @throws IllegalArgumentException If the server address is
	 *             <code>null</code>.
	 */
	public ClientConnection create(@NotNull final RaysonServerAddress serverAddr) {
		if (serverAddr == null)
			throw new IllegalArgumentException("serverAddr should not be null");
		final ClientConnection conn = new ClientConnection(getNextConnId(), serverAddr);
		return conn;
	}

	/**
	 * @return Next connection unique id.
	 */
	long getNextConnId() {
		return uid.getAndIncrement();
	}

	/**
	 * Initialize this pool. Invoked by {@link Rayson} client only.
	 */
	public void initialize() {
		monitor.start();
	}

	/**
	 * Add an connected connection to this pool.And managed by this pool.
	 * 
	 * @param conn A connection which is connected to remote server.
	 * @throws IllegalArgumentException If the connection is <code>null</code>.
	 */
	public void put(@NotNull final ClientConnection conn) {
		if (conn == null)
			throw new IllegalArgumentException("conn should not be null");
		map.put(conn.getId(), conn);
	}

	/**
	 * Reused a connection.
	 * 
	 * @param conn Connection to tell this pool to reuse.
	 * @return Reused connection. <code>null</code> means the giving connection
	 *         can not be reused.
	 * @throws IllegalArgumentException If the connection is <code>null</code>.
	 */
	public synchronized ClientConnection reuse(@NotNull final ClientConnection conn) {
		if (conn == null)
			throw new IllegalArgumentException("conn should not be null");
		if (conn.isRecycled())
			return null;
		// Before reuse, we should reset the contact time of the target
		// connection.
		conn.touch();
		return conn;
	}

	/**
	 * Recycle an connection. The associated connection will be closed
	 * asynchronously by this pool.If associated connection is not managed by
	 * this pool, it will do nothing.
	 * 
	 * @param connectionId Connection id to be removed.
	 */
	synchronized void recycle(final long connectionId) {
		final ClientConnection conn = map.remove(connectionId);
		if (conn == null)
			return;
		conn.recycle();
		monitor.addTask(conn);
	}
}