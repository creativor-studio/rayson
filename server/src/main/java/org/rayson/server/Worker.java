
package org.rayson.server;

import java.util.concurrent.TimeUnit;

import org.rayson.api.annotation.ThreadSafe;
import org.rayson.api.exception.ConnectionClosedException;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.server.exception.InternalServerError;
import org.rayson.server.http.HttpServerRequestImpl;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * Worker is a thread used to handling {@link HttpServerRequest}s send from
 * client. It is managed by {@link WorkerManager}.
 * 
 * @author Nick Zhang
 *
 */
@ThreadSafe(false)
public class Worker implements Manager, Runnable {

	private final Container container;

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(Worker.class);
	private static final int MAX_TAKING_REQUEST_TIME = 1000 * 60 * 3;

	private boolean running;
	/**
	 * Underling java thread of this worker .
	 */
	protected Thread thread;

	private final long id;

	private WorkerManager manager;

	/**
	 * 
	 * @param workerId Unique id of the new worker.
	 * @param manager Manager to manage the new worker.
	 * @param container Server container instance.
	 */
	public Worker(final long workerId, WorkerManager manager, final Container container) {
		super();
		this.id = workerId;
		this.container = container;
		this.manager = manager;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public String getName() {
		return "Worker " + id + " of " + container.getServer().getName();
	}

	private void handleRequest(final HttpServerRequestImpl request) throws ConnectionClosedException, ServiceException {

		final HttpServerResponse response = this.container.getServiceManager().doFilter(request);
		// Add response to connection.
		((HttpServerRequestImpl) request).getConnection().addResponse(response);
	}

	@Override
	public final void run() {
		while (running) {
			// Reset the interrupted status.
			Thread.interrupted();
			try {
				final HttpServerRequestImpl request = (HttpServerRequestImpl) this.container.getRequestManager().take(MAX_TAKING_REQUEST_TIME,
						TimeUnit.MILLISECONDS);
				if (request == null)
					continue;
				manager.notifyBeginRequest(this, request);
				try {
					handleRequest(request);
				} catch (final Throwable e) {
					LOGGER.debug("handle request got error", e);
				} finally {
					manager.notifyFinishRequest(this, request);
				}
			} catch (final InterruptedException e) {
				// Ignore.
				continue;
			}
		}

		this.manager.notifyQuit(this);

		LOGGER.error(getName() + " quit");
	}

	@Override
	public void start() throws InternalServerError {
		running = true;
		thread = new Thread(this, getName());
		thread.start();
	}

	/**
	 * @return <code>true</code> if this worker is running.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return The id of this worker.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Notify this worker to quit from running.
	 */
	@Override
	public void stop() {
		this.running = false;
		this.thread.interrupt();
	}
}