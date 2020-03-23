
package org.rayson.api.server;

import org.rayson.api.FilterChain;
import org.rayson.api.server.exception.ServiceException;

/**
 * Filter chain on server side to do filter RPC request and response.
 * 
 * @author Nick Zhang
 *
 */
public abstract interface ServiceFilterChain extends FilterChain {
	/**
	 * Causes the next filter in the chain to be invoked, or if the calling
	 * filter is the last filter in the chain, causes the resource at the end of
	 * the chain to be invoked.
	 * 
	 * @param request HTTP request to be filtered.
	 * @param response HTTP request to be filtered.
	 * @throws ServiceException If the filtering process got error.
	 */
	public abstract void doFilter(ServerRequest request, ServerResponse response) throws ServiceException;
}
