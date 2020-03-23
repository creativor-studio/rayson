
package org.rayson.api.client;

import org.rayson.api.FilterChain;
import org.rayson.api.exception.ClientFilterException;

/**
 * Filter chain on client side to do filter RPC request and response.
 * 
 * @author Nick Zhang
 *
 */
public abstract interface ClientFilterChain extends FilterChain {
	/**
	 * Causes the next filter in the chain to be invoked, or if the calling
	 * filter is the last filter in the chain, causes the resource at the end of
	 * the chain to be invoked.
	 * 
	 * @param request RPC request to be filtered.
	 * @param response RPC request to be filtered.
	 * @throws ClientFilterException If the filtering process got error.
	 */
	public abstract void doFilter(ClientRequest request, ClientResponse response) throws ClientFilterException;
}
