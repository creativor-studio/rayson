/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import org.rayson.api.Filter;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.exception.ClientFilterException;

/**
 * Client side service {@link Filter}.
 * 
 * @author Nick Zhang
 */
public interface ClientFilter extends Filter {
	/**
	 * The doFilter method of the Filter is called each time a request/response
	 * pair is passed through the chain due to a client request for a service at
	 * the end of the chain.
	 * 
	 * @param request RPC request.
	 * @param response RPC response.
	 * @param chain Filter chain.
	 * @throws ClientFilterException If the filtering got error.
	 */
	public void doFilter(final ClientRequest request, final ClientResponse response, final ClientFilterChain chain) throws ClientFilterException;

	/**
	 * Initialize this filter.
	 * 
	 * @param proxy RPC proxy instance associated with this filter.
	 */
	public abstract void init(@NotNull Proxy proxy);
}
