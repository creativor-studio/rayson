/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import org.rayson.api.Filter;
import org.rayson.api.annotation.NotNull;

/**
 * Server side HTTP {@link Filter}.
 *
 * @author creativor
 */
public interface HttpServerFilter extends Filter {
	/**
	 * The doFilter method of the Filter is called each time a request/response
	 * pair is passed through the chain due to a client request for a service at
	 * the end of the chain.
	 * 
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @param chain Filter chain.
	 */
	public abstract void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain);

	/**
	 * Initialize this filter.
	 * 
	 * @param server Rayson server instance associated with this filter.
	 */
	public abstract void init(@NotNull Server server);
}
