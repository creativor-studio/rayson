/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.filter;

import org.rayson.api.server.HttpFilterChain;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.Server;
import org.rayson.server.http.HttpServerRequestImpl;

/**
 * First {@link HttpServerFilter} in the chain.
 * 
 */
class FirstHttpFilter implements HttpServerFilter {

	private final ServiceManagerImpl manager;

	
	FirstHttpFilter(ServiceManagerImpl manager) {
		this.manager = manager;
	}

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain) {

		// Do initialize job first.
		((HttpServerRequestImpl) request).setupParameters();
		chain.doFilter(request, response);
	}

	@Override
	public void init(Server server) {
		// Do nothing.
	}
}