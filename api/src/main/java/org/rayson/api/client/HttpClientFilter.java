/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import org.rayson.api.Filter;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.exception.ClientFilterException;

/**
 * Client side HTTP {@link Filter}.
 *
 * @author creativor
 */
public interface HttpClientFilter extends Filter {

	/**
	 * The doFilter method of the Filter is called each time a request/response
	 * pair is passed through the chain due to a client request for a service at
	 * the end of the chain.
	 * 
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @param chain Filter chain.
	 * @throws ClientFilterException If the filtering got error.
	 */
	public abstract void doFilter(final HttpClientRequest request, final HttpClientResponse response, final HttpFilterChain chain) throws ClientFilterException;

	/**
	 * Initialize this filter.
	 * 
	 * @param proxy RPC proxy instance associated with this filter.
	 */
	public abstract void init(@NotNull Proxy proxy);
}