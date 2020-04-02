/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.filter;

import java.lang.reflect.Method;

import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.server.HttpFilterChain;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.RpcContext;
import org.rayson.api.server.Server;
import org.rayson.server.http.HttpServerRequestImpl;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * First {@link HttpServerFilter} in the chain.
 * 
 */
class FirstHttpFilter implements HttpServerFilter {

	private static final Method CLEAR_RPC_CONTEXT_METHOD;
	private static final Method SET_RPC_CONTEXT_METHOD;
	private static final Logger LOG = RaysonLoggerFactory.getLogger(FirstHttpFilter.class);

	static {
		try {
			SET_RPC_CONTEXT_METHOD = RpcContext.class.getDeclaredMethod("setupContext");
			SET_RPC_CONTEXT_METHOD.setAccessible(true);
			CLEAR_RPC_CONTEXT_METHOD = RpcContext.class.getDeclaredMethod("clearContext");
			CLEAR_RPC_CONTEXT_METHOD.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException("Failed to found context setting methods in " + RpcContext.class, e);
		}
	}

	private final ServiceManagerImpl manager;

	FirstHttpFilter(ServiceManagerImpl manager) {
		this.manager = manager;
	}

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain) {
		try {
			// Do initialize job first.
			((HttpServerRequestImpl) request).setupParameters();
			// Setup RPC context.
			SET_RPC_CONTEXT_METHOD.invoke(null);
			try {
				chain.doFilter(request, response);
			} finally {
				CLEAR_RPC_CONTEXT_METHOD.invoke(null);
			}
		} catch (Throwable e) {
			LOG.error("Got internal server error", e);
			response.setException(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
	}

	@Override
	public void init(Server server) {
		// Do nothing.
	}
}