/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.http;

import org.rayson.api.server.HttpFilterChain;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;

/**
 * Default implementation of {@link HttpFilterChain}.
 * 
 * @author Nick Zhang
 *
 */
public class DefaultHttpFilterChain implements HttpFilterChain {

	private final HttpFilterChain next;
	private final HttpServerFilter filter;

	/**
	 * @param filter Filter instance associated with this new chain.
	 * @param next Next chain item of this new chain.
	 */
	public DefaultHttpFilterChain(final HttpServerFilter filter, final HttpFilterChain next) {
		this.filter = filter;
		this.next = next;
	}

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response) {
		this.filter.doFilter(request, response, next);
	}

}