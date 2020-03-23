/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.ThreadSafe;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.ServiceFilter;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;

/**
 * An manager role in Rayson server to manage services.
 * 
 * @author creativor
 */
public interface ServiceManager extends Manager {

	/**
	 * Add global HTTP filter to this filter manager.<br>
	 * 
	 * @param type Filter type class.
	 * @throws ServiceException If failed to add the filter.
	 */
	public void addHttpFilter(Class<? extends HttpServerFilter> type) throws ServiceException;

	/**
	 * Add global service filter to this filter manager.<br>
	 * 
	 * @param type Filter type class.
	 * @throws ServiceException If failed to add the filter.
	 */
	public void addServiceFilter(Class<? extends ServiceFilter> type) throws ServiceException;

	/**
	 * Get protocol mirror of giving protocol interface.
	 * 
	 * @param protocol Protocol interface to find it's mirror.
	 * @return Found matched mirror. Or <code>null</code> if not found matched.
	 * @throws IllegalArgumentException If the giving protocol is null or is not
	 *             an interface.
	 */
	public ProtocolMirror getMirror(@NotNull final Class<? extends Protocol> protocol) throws IllegalArgumentException;

	/**
	 * @return Protocol mirrors which associated services managed by this
	 *         manager.
	 */
	public ProtocolMirror[] getProtocols();

	/**
	 * Do filter an request
	 * 
	 * @param request Request to be filtered.
	 * @return Request filter result, as a response.
	 * @throws ServiceException If the handling the request and got server side
	 *             error.
	 */
	public HttpServerResponse doFilter(@NotNull final HttpServerRequest request) throws ServiceException;

	/**
	 * Register services.
	 * 
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
	@ThreadSafe(true)
	public void registerService(@NotNull final Protocol service)
			throws DuplicateServiceException, ServiceException, IllegalArgumentException, InvalidApiException;

}