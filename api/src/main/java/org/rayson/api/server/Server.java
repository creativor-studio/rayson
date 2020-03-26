/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.protocol.info.ServerInfo;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;

/**
 * Rayson API Server interface, which provide {@link Service}s.
 * 
 * @author Nick Zhang
 *
 */
public interface Server {
	/**
	 * @return Information about this server.
	 */
	public ServerInfo getInfo();

	/**
	 * Add global HTTP filter to this server.<br>
	 * 
	 * @param filter Filter definition.
	 * @throws ServiceException If failed to add the filter.
	 */
	public void addHttpFilter(Class<? extends HttpServerFilter> filter) throws ServiceException;

	/**
	 * Add global service filter to this server.<br>
	 * 
	 * @param filter Filter definition.
	 * @throws ServiceException If failed to add the filter.
	 */
	public void addServiceFilter(Class<? extends ServiceFilter> filter) throws ServiceException;

	/**
	 * @return The configuration of this server.
	 */
	public ServerConfig getConfig();

	/**
	 * @return Name of this server.
	 */
	public String getName();

	/**
	 * @return The port number of this server.
	 */
	public int getPortNumber();

	/**
	 * Register an service instance into this server.
	 * 
	 * @see Service About rayson service.
	 * @param service Service instance.
	 * @throws DuplicateServiceException If the service with same protocol
	 *             interface is exists.
	 * @throws ServiceException If the service instance is not an illegal one.
	 *             Or failed to register the service.
	 * @throws IllegalArgumentException If the service instance is null.
	 * @throws InvalidApiException If the Rayson protocol interface of the given
	 *             service instance is not fulfill Rayson RPC protocol
	 *             requirement
	 */
	public void registerService(Protocol service) throws DuplicateServiceException, ServiceException, InvalidApiException, IllegalArgumentException;

}