/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.filter;

import java.lang.reflect.Method;

import org.rayson.api.Protocol;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.server.HttpFilterChain;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.RpcContext;
import org.rayson.api.server.Server;
import org.rayson.api.server.ServerRequest;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.server.http.HttpServerRequestImpl;
import org.rayson.server.http.HttpServerResponseImpl;
import org.rayson.server.rpc.ServerRequestImpl;
import org.rayson.server.rpc.ServerResponseImpl;
import org.rayson.share.exception.SerializationException;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * Last {@link HttpServerFilter} in the chain.
 * 
 */
class LastHttpFilter implements HttpServerFilter {
	private static final Method CLEAR_RPC_CONTEXT_METHOD;
	private static final Method SET_RPC_CONTEXT_METHOD;
	private static final Logger LOG = RaysonLoggerFactory.getLogger(LastHttpFilter.class);

	static {
		try {
			SET_RPC_CONTEXT_METHOD = RpcContext.class.getDeclaredMethod("setupContext", ServerRequest.class);
			SET_RPC_CONTEXT_METHOD.setAccessible(true);
			CLEAR_RPC_CONTEXT_METHOD = RpcContext.class.getDeclaredMethod("clearContext");
			CLEAR_RPC_CONTEXT_METHOD.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException("Failed to found context setting methods in " + RpcContext.class, e);
		}
	}

	private final ServiceManagerImpl manager;

	public LastHttpFilter(ServiceManagerImpl serviceManagerImpl) {
		manager = serviceManagerImpl;
	}

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain) {
		String url = request.getUrl();

		MethodChain methodChain = manager.getMethodChain(url);
		if (methodChain == null) {
			response.setException(HttpResponseStatus.NOT_FOUND, "No API found");
			return;
		}

		DefaultFilterChain serviceChain = methodChain.getChain();

		Protocol service;
		ProtocolMirror protocolMirror;
		MethodMirror methodMirror;
		service = serviceChain.getService();
		protocolMirror = serviceChain.getProtocolMirror();
		methodMirror = protocolMirror.getMethod(methodChain.getMethod().getName());

		if (methodMirror == null) {
			response.setException(HttpResponseStatus.NOT_FOUND, "No method " + methodChain.getMethod().getName() + " found in protocol");
			return;
		}

		HttpServerRequestImpl requestImpl = (HttpServerRequestImpl) request;
		ServerRequestImpl serviceRequest = new ServerRequestImpl(requestImpl);
		ServerResponseImpl serviceResponse = new ServerResponseImpl(serviceRequest, methodMirror);

		try {
			// Setup service request first.
			serviceRequest.setupRpcParams(service, protocolMirror, methodMirror);

			// Setup RPC context.
			SET_RPC_CONTEXT_METHOD.invoke(null, serviceRequest);
			try {
				// Do service filter.
				serviceChain.doFilter(serviceRequest, serviceResponse);
			} finally {
				CLEAR_RPC_CONTEXT_METHOD.invoke(null);
			}
		} catch (ServiceException e) {
			response.setException(HttpResponseStatus.SERVICE_UNAVAILABLE, e.getMessage());
			return;
		} catch (SerializationException e) {
			response.setException(HttpResponseStatus.RAYSON_WRONG_PROTOCOL_FORMAT, e.getMessage());
			return;
		} catch (Throwable e) {
			LOG.error("Got internal server error", e);
			response.setException(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}

		// Then setup the HTTP response.
		serviceResponse.setupHttpResponse((HttpServerResponseImpl) response);

		return;
	}

	@Override
	public void init(Server server) {
		// Do nothing.
	}
}