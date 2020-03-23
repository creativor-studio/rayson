
package org.rayson.client.transport;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.rayson.client.ClientSystem;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.rayson.share.transport.NioSelector;
import org.rayson.share.transport.NioSelectorBase;
import org.slf4j.Logger;

/**
 * Client side {@link NioSelector}.
 * 
 * @author Nick Zhang
 *
 */
public class ClientSelector extends NioSelectorBase implements NioSelector {

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(ClientSelector.class);

	private final Lock acceptLock;

	private final List<Runnable> acceptTask;

	/**
	 * Construct a new client side selector with native underling
	 * {@link Selector} and it's name.
	 * 
	 * @param selector Native underling {@link Selector}.
	 * @param name Name of this selector.
	 */
	public ClientSelector(final Selector selector, final String name) {
		super(name, selector);
		acceptLock = new ReentrantLock();
		acceptTask = new ArrayList<>();
	}

	/**
	 * Accept a new connection.
	 * 
	 * @param conn Connect to accept.
	 */
	void accept(final ClientConnection conn) {
		acceptLock.lock();
		try {
			acceptTask.add(new Runnable() {

				@Override
				public void run() {

					try {
						final SelectionKey sk = conn.getChannel().registerInterest(selector, SelectionKey.OP_CONNECT);
						sk.attach(conn);
						conn.getChannel().bindSelectionKey(sk);
					} catch (final Throwable e) {
						conn.notifyConnectFailed(e);
					}

				}
			});
			selector.wakeup();

		} finally {
			acceptLock.unlock();
		}
	}

	@Override
	protected void preSelect() {
		pollAcceptTasks();
	}

	@Override
	protected void doAccept(SelectionKey key) {
		// Do nothing.
	}

	@Override
	protected void doConnect(final SelectionKey key) {
		final ClientConnection conn = (ClientConnection) key.attachment();
		try {
			if (conn.getSocketChannel().isConnectionPending() && conn.getSocketChannel().finishConnect()) {
				conn.getChannel().interestOps(SelectionKey.OP_READ);
				LOGGER.info("Connected " + conn);
				conn.notifyConnected();
			}
		} catch (final Throwable e) {
			conn.notifyConnectFailed(e);
		}
	}

	@Override
	protected void doRead(final SelectionKey key) {
		final ClientConnection conn = (ClientConnection) key.attachment();
		try {
			conn.asyncRead();
		} catch (final Throwable e) {
			if (!(e instanceof IOException))
				LOGGER.error("Async write from connection got error", e);

			try {
				// First cancel the read event.
				conn.getChannel().unInterestRead();
				conn.notifyClosing(e);
				// The remove connection.
				ClientSystem.getSystem().getConnectionPool().recycle(conn.getId());
			} catch (Throwable e1) {
				// Ignore it.
			}
		}
	}

	@Override
	protected void doWrite(final SelectionKey key) {
		final ClientConnection conn = (ClientConnection) key.attachment();
		try {
			conn.asyncWrite();
		} catch (final Throwable e) {
			if (!(e instanceof IOException))
				LOGGER.error("Async write from connection got error", e);

			try {
				// First cancel the write event.
				conn.getChannel().unInterestWrite();
				conn.notifyClosing(e);
				// The remove connection.
				ClientSystem.getSystem().getConnectionPool().recycle(conn.getId());
			} catch (Throwable e1) {
				// Ignore it.
			}
		}
	}

	private void pollAcceptTasks() {
		acceptLock.lock();
		try {
			if (acceptTask.isEmpty())
				return;

			for (final Iterator<Runnable> iter = acceptTask.iterator(); iter.hasNext();) {
				final Runnable task = iter.next();
				iter.remove();

				try {
					task.run();

				} catch (final Exception e) {
					continue;
				}
			}

		} finally {
			acceptLock.unlock();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				doSelect();
			} catch (final Throwable e) {
				LOGGER.error(getName() + " got runtime error", e);
			}
		}
	}

}