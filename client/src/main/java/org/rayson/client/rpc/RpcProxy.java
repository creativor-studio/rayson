/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.client.ClientRequest;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.Proxy;
import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.exception.RpcException;
import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpHeader;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpRequestLine;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.struct.KeyValues;
import org.rayson.client.ClientSystem;
import org.rayson.rson.element.ObjectElement;
import org.rayson.share.client.HttpClientRequestImpl;
import org.rayson.share.http.HttpHeaderImpl;
import org.rayson.share.http.HttpRequestLineImpl;
import org.rayson.share.serial.rson.RsonUtil;
import org.rayson.share.util.MirrorTool;
import org.slf4j.Logger;

/**
 * An {@link InvocationHandler} used to invoke RPC invocation.This role will
 * manipulate transport layer directly.
 * 
 * @author creativor
 */
public class RpcProxy implements InvocationHandler, Proxy {

	// preloaded Method objects for the methods in java.lang.Object
	private static final Method EQUALS_METHOD;
	private static final Method HASHCODE_METHOD;
	private static final Method TOSTRING_METHOD;
	private static final Logger LOG = ClientSystem.getSystem().getLogger();

	static {
		try {
			HASHCODE_METHOD = Object.class.getMethod("hashCode");
			EQUALS_METHOD = Object.class.getMethod("equals", new Class[] { Object.class });
			TOSTRING_METHOD = Object.class.getMethod("toString");
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodError(e.getMessage());
		}
	}

	private final ProxyConfig config;
	private final ClientFilterManager filterManager;
	private final RpcManager manager;
	private final Class<? extends Protocol> protocol;

	private final ProtocolMirror protocolMirror;

	private final RaysonServerAddress serverAddr;

	private final String serviceUrl;

	RpcProxy(@NotNull final RaysonServerAddress serverAddr, @NotNull final Class<? extends Protocol> protocol, final ProxyConfig config,
			final RpcManager manager) throws IllegalArgumentException, InvalidApiException {
		this.serverAddr = serverAddr;
		this.protocol = protocol;
		this.config = config;
		this.manager = manager;

		// Initialize filter manager.

		this.filterManager = new ClientFilterManager(this);

		this.filterManager.addFilter(config.getFilters());

		this.filterManager.addHttpFilter(config.getHttpFilters());

		protocolMirror = MirrorTool.load(protocol);
		this.serviceUrl = "/" + protocol.getName();
	}

	HttpClientRequest creatHttpRequest(@NotNull final ClientRequest request) {
		final HttpClientRequestImpl httpReq = new HttpClientRequestImpl(manager.nextCallId());

		final MethodMirror method = getProtocolMirror().getMethod(request.getMethod().getName());
		final HttpRequestLine startLine = new HttpRequestLineImpl(serviceUrl + HttpConstants.SLASH_TOKEN + method.getName());
		httpReq.setStartLine(startLine);

		// 2. Setup headers.

		final Collection<KeyValues> headers = request.getAllHeaders();
		for (final KeyValues header : headers) {
			for (final String value : header.getValues()) {
				httpReq.addHeader(new HttpHeaderImpl(header.getKey(), value));
			}
		}

		// 3. Setup content type.
		HttpHeader conentType = ClientSystem.getSystem().getContentTypeHeader();
		httpReq.setHeader(conentType);

		// 4. Setup body.
		byte[] body;
		try {
			ObjectElement rson = RsonUtil.toRpcRequest(request.getArguments(), method);
			body = ClientSystem.getSystem().getContentCoder().encodeRequest(rson, HttpMessage.CHARSET);
		} catch (final Exception e) {
			// we checked the invalid API exception when creating the RPC proxy,
			// so the exception should not be occurred here so it will be
			// treated as runtime exception.
			throw new RuntimeException("Serialize the request " + request + " error", e);
		}
		httpReq.setBody(body);
		return httpReq;
	}

	@Override
	public ProxyConfig getConfig() {
		return config;
	}

	/**
	 * @return the filter manager of this proxy.
	 */
	public ClientFilterManager getFilterManager() {
		return filterManager;
	}

	@Override
	public Class<? extends Protocol> getProtocol() {
		return protocol;
	}

	/**
	 * @return the protocol mirror of this proxy.
	 */
	ProtocolMirror getProtocolMirror() {
		return protocolMirror;
	}

	@Override
	public RaysonServerAddress getServerAddr() {
		return serverAddr;
	}

	/**
	 * @return the service url of this proxy.
	 */
	String getServiceUrl() {
		return serviceUrl;
	}

	private static int proxyHashCode(Object proxy) {
		return System.identityHashCode(proxy);
	}

	private static boolean proxyEquals(Object proxy, Object obj) {
		if (obj == null)
			return false;
		if (proxy != obj)
			return false;
		return true;
	}

	private static String proxyToString(Object proxy) {
		return RpcProxy.class.getName() + proxy.getClass().getSimpleName() + '@' + Integer.toHexString(proxy.hashCode());
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws RpcException, IOException, ClientFilterException {

		Class<?> declaringClass = method.getDeclaringClass();

		// Object method.
		if (declaringClass == Object.class) {
			if (method == HASHCODE_METHOD || method.equals(HASHCODE_METHOD)) {
				return proxyHashCode(proxy);
			} else if (method == EQUALS_METHOD || method.equals(EQUALS_METHOD)) {
				return proxyEquals(proxy, args[0]);
			} else if (method == TOSTRING_METHOD || method.equals(TOSTRING_METHOD)) {
				return proxyToString(proxy);
			} else {
				throw new InternalError("unexpected Object method dispatched: " + method);
			}
		}

		// Proxy method.
		if (declaringClass == Proxy.class) {
			try {
				return method.invoke(this, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException("Failed to invoke proxy method", e);
			}
		}

		// Rpc invocation.

		final ClientRequestImpl request = new ClientRequestImpl(method, args);
		final ClientResponseImpl response = new ClientResponseImpl(getProtocolMirror().getMethod(request.getMethod().getName()));
		filterManager.doFilter(request, response);
		return response.getResult();

	}
}