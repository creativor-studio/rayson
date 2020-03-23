
package org.rayson.api.server;

import org.rayson.api.FilterChain;
import org.rayson.api.exception.ClientFilterException;

/**
 * Filter chain on client side to do filter HTTP request and response.
 * 
 * @author Nick Zhang
 *
 */
public abstract interface HttpFilterChain extends FilterChain {
	/**
	 * Causes the next filter in the chain to be invoked, or if the calling
	 * filter is the last filter in the chain, causes the resource at the end of
	 * the chain to be invoked.
	 * 
	 * @param request HTTP request to be filtered.
	 * @param response HTTP request to be filtered.
	 * @throws ClientFilterException If the filtering process got error.
	 */
	public abstract void doFilter(HttpServerRequest request, HttpServerResponse response) throws ClientFilterException;
}
