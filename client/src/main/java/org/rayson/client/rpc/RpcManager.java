/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.atomic.AtomicLong;

import org.rayson.api.Protocol;
import org.rayson.api.client.Proxy;
import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.InvalidApiException;

/**
 * The role used to manage RPC layer objects.
 * 
 * @author creativor
 */
public final class RpcManager {

	private final AtomicLong callId;

	/**
	 * Construct an empty RPC manager.
	 */
	public RpcManager() {
		callId = new AtomicLong();
	}

	long nextCallId() {
		return callId.getAndIncrement();
	}

	/**
	 * Returns the {@link org.rayson.api.client.Proxy} instance for the
	 * specified proxy argument.
	 * 
	 * @param proxy RPC proxy instance stands for an {@link Protocol}.
	 * @return The Rayson RPC proxy instance.
	 * @throws IllegalArgumentException if the argument is not a Rayson RPC
	 *             proxy instance.
	 * @throws SecurityException if a security manager, <em>s</em>, is present
	 *             and the caller's class loader is not the same as or an
	 *             ancestor of the class loader for the invocation handler and
	 *             invocation of {@link SecurityManager#checkPackageAccess
	 *             s.checkPackageAccess()} denies access to the invocation
	 *             handler's class.
	 */
	public Proxy getProxy(Protocol proxy) throws IllegalArgumentException, SecurityException {

		if (proxy == null)
			throw new IllegalArgumentException("arugment proxy should not be null");

		InvocationHandler handler = java.lang.reflect.Proxy.getInvocationHandler(proxy);
		if (!(handler instanceof org.rayson.api.client.Proxy))
			throw new IllegalArgumentException("proxy arugment is not an instance of " + Proxy.class);
		return (Proxy) handler;
	}

	/**
	 * Create an new RPC proxy of given protocol and server address.
	 * 
	 * @param <T> Protocol type.
	 * @param serverAddr Address of the remote rayson server.
	 * @param protocol Protocol interface class. Must be an java Interface.
	 * @param config Configuration of the target proxy.
	 * @return RPC proxy.
	 * @throws IllegalArgumentException If arguments format is wrong.
	 * @throws InvalidApiException If the given {@link Protocol} is an invalid
	 */
	public <T extends Protocol> T createProxy(final RaysonServerAddress serverAddr, final Class<T> protocol, final ProxyConfig config)
			throws IllegalArgumentException, InvalidApiException {

		if (serverAddr == null)
			throw new IllegalArgumentException("serverAddr should not be null");
		if (protocol == null)
			throw new IllegalArgumentException("protocol should not be null");
		if (config == null)
			throw new IllegalArgumentException(" config should not be null");

		if (!protocol.isInterface())
			throw new IllegalArgumentException("RPC protocol must be an interface");

		return (T) java.lang.reflect.Proxy.newProxyInstance(protocol.getClassLoader(), new Class[] { protocol, org.rayson.api.client.Proxy.class },
				new RpcProxy(serverAddr, protocol, config, this));
	}
}