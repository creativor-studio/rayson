/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import org.rayson.api.Filter;
import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.server.exception.ServiceException;

/**
 * Server side service {@link Filter}.
 *
 * @author creativor
 */
public interface ServiceFilter extends Filter {
	/**
	 * The doFilter method of the Filter is called each time a request/response
	 * pair is passed through the chain due to a client request for a service at
	 * the end of the chain.
	 * 
	 * @param request RPC request.
	 * @param response RPC response.
	 * @param chain Filter chain.
	 * @throws ServiceException If the filtering got error.
	 */
	public abstract void doFilter(ServerRequest request, ServerResponse response, ServiceFilterChain chain) throws ServiceException;

	/**
	 * Initialize this filter.
	 * 
	 * @param server Rayson server instance associated with this filter.
	 * @param service Service instance associated with this filter.
	 * @throws ServiceException If failed to initialized this service filter.
	 */
	public abstract void init(@NotNull Server server, Protocol service) throws ServiceException;
}