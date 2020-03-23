
package org.rayson.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.rayson.api.server.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An manager role used manage {@link HttpServerRequest}s send from client.
 * 
 * @author Nick Zhang
 *
 */
public class RequestManager implements Manager {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestManager.class);
	private final Container container;

	private final LinkedBlockingQueue<HttpServerRequest> requests;

	/**
	 * 
	 * @param container Server container which manage this request manager.
	 */
	public RequestManager(final Container container) {
		this.container = container;
		requests = new LinkedBlockingQueue<HttpServerRequest>();
	}

	/**
	 * Add an request to this manager.
	 * 
	 * @param request Request to be managed by this manager.
	 */
	public void add(final HttpServerRequest request) {
		requests.add(request);
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public String getName() {
		return "Request Manager of " + container.getName();
	}

	@Override
	public void start() {
		LOGGER.info(getName() + " starting ...");

	}

	@Override
	public void stop() {
		LOGGER.info(getName() + " stopping ...");
	}

	/**
	 * Retrieves and removes the head of this queue, waiting up to the specified
	 * wait time if necessary for an element to become available.
	 * 
	 * @param timeout how long to wait before giving up, in units of unit
	 * @param unit a TimeUnit determining how to interpret the timeout parameter
	 * @return The head of this queue, or null if the specified waiting time
	 *         elapses before an element is available
	 * @throws InterruptedException if interrupted while waiting
	 */
	public HttpServerRequest take(long timeout, TimeUnit unit) throws InterruptedException {
		return requests.poll(timeout, unit);
	}
}