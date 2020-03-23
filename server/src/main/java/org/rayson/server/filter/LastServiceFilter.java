/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.filter;

import java.lang.reflect.InvocationTargetException;

import org.rayson.api.Protocol;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.server.Server;
import org.rayson.api.server.ServerRequest;
import org.rayson.api.server.ServerResponse;
import org.rayson.api.server.ServiceFilter;
import org.rayson.api.server.ServiceFilterChain;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.api.struct.RpcParameters;
import org.rayson.server.rpc.ServerRequestImpl;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * The last {@link ServiceFilter} in the whole {@link ServiceFilterChain}. It
 * will do invoke the RPC request of giving {@link Protocol} service
 * implementation.
 * 
 * @author creativor
 */
class LastServiceFilter implements ServiceFilter {

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(LastServiceFilter.class);
	private Server server;
	private Protocol service;

	public LastServiceFilter() {
	}

	@Override
	public void doFilter(final ServerRequest request, final ServerResponse response, final ServiceFilterChain chain) throws ServiceException {
		// Just invoke the request in service.

		if (LOGGER.isDebugEnabled())
			LOGGER.debug(request + " invoking in " + LastServiceFilter.class);
		// Do invoking the request.

		final ServerRequestImpl requsetImpl = (ServerRequestImpl) request;

		try {
			RpcParameters parameters = requsetImpl.getParameters();
			Object[] arguments = parameters.toArguments();
			response.setResult(requsetImpl.getMethodMirror().getSource().invoke(service, arguments));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			response.setException(new RpcException(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
			return;
		} catch (final InvocationTargetException e) {

			final Throwable cause = e.getTargetException();
			if (cause instanceof RpcException) {
				response.setException((RpcException) cause);
			} else {
				response.setException(new RpcException(HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode(), cause.getMessage()));
			}

			return;
		}

		// In fact , it is always null.
		if (chain != null)
			chain.doFilter(request, response);

	}

	@Override
	public void init(Server server, Protocol service) throws ServiceException {
		this.server = server;
		this.service = service;
	}
}