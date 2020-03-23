
package org.rayson.server;

import org.rayson.server.exception.InternalServerError;
import org.rayson.server.filter.ServiceManagerImpl;
import org.rayson.server.transport.ConnectionManager;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * An container to hold key modules used in Rayson server system.
 * 
 * @author Nick Zhang
 */
public class Container {
	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(Container.class);

	private final ConnectionManager connectionManager;

	private final ServiceManager serviceManager;

	private final RequestManager requestManager;
	private boolean running = false;

	private final RaysonServer server;

	private final WorkerManager workerManager;

	Container(final RaysonServer server) {
		this.server = server;
		this.connectionManager = new ConnectionManager(this);
		this.requestManager = new RequestManager(this);
		this.serviceManager = new ServiceManagerImpl(this);
		this.workerManager = new WorkerManager(this);
	}

	/**
	 * @return Connection manager in Rayson server.
	 */
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	/**
	 * @return Service manager in Rayson server.
	 */
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	/**
	 * @return Name of this container.
	 */
	public String getName() {
		return "Container of " + this.server.getName();
	}

	/**
	 * @return Request manager in Rayson server.
	 */
	public RequestManager getRequestManager() {
		return requestManager;
	}

	/**
	 * @return The rayson server instance maintained by this container.
	 */
	public RaysonServer getServer() {
		return server;
	}

	/**
	 * @return Worker manager in Rayson server.
	 */
	public WorkerManager getWorkerManager() {
		return workerManager;
	}

	/**
	 * Start this container.
	 * 
	 * @throws InternalServerError If the starting process got error.
	 */
	void start() throws InternalServerError {
		LOGGER.info(getName() + " starting ...");
		this.requestManager.start();
		this.serviceManager.start();
		this.connectionManager.start();
		this.workerManager.start();
		running = true;
	}

	/**
	 * Stop this container.
	 * 
	 * @throws InternalServerError If the stopping process got error.
	 */
	public void stop() throws InternalServerError {
		if (!this.running)
			return;

		LOGGER.info(getName() + " stopping ...");

		this.workerManager.stop();
		this.connectionManager.stop();
		this.serviceManager.stop();
		this.requestManager.stop();
		this.running = false;
	}

	@Override
	public String toString() {
		return getName();
	}
}