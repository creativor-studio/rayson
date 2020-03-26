/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.Nullable;
import org.rayson.api.client.Proxy;
import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.InvalidApiException;

/**
 * Rayson client.
 * 
 * @author creativor
 * @see Proxy About RPC invoking proxy.
 */
public final class Rayson {
	/**
	 * Create an proxy of protocol.
	 * 
	 * @param <T> Protocol type.
	 * @param serverAddr Address of the remote rayson server.
	 * @param protocol Protocol interface class. Must be an java Interface.
	 * @return An proxy that used to call remote server RPC invocation, which
	 *         defined in protocol. It is also an instance of {@link Proxy}.
	 * @throws IllegalArgumentException If arguments format is wrong.
	 * @throws InvalidApiException If the given {@link Protocol} is an invalid
	 *             API.
	 * @see Proxy About proxy.
	 */
	public static <T extends Protocol> T createProxy(@NotNull final RaysonServerAddress serverAddr, @NotNull final Class<T> protocol)
			throws IllegalArgumentException, InvalidApiException {
		ClientSystem.getSystem().lazyInit();
		return ClientSystem.getSystem().getRpcManager().createProxy(serverAddr, protocol, new ProxyConfig());
	}

	/**
	 * Create an new RPC proxy of specified protocol.
	 * 
	 * @param <T> Protocol type.
	 * @param serverAddr Address of the remote rayson server.
	 * @param protocol Protocol interface class. Must be an java Interface.
	 * @param config RPC configuration.
	 * @return An proxy that used to call remote server RPC invocation, which
	 *         defined in protocol.It is also an instance of {@link Proxy}.
	 * @throws IllegalArgumentException If arguments format is wrong.
	 * @throws InvalidApiException If the given {@link Protocol} is an invalid
	 *             API.
	 * @see Proxy About proxy.
	 */
	public static <T extends Protocol> T createProxy(@NotNull final RaysonServerAddress serverAddr, @NotNull final Class<T> protocol,
			@Nullable final ProxyConfig config) throws IllegalArgumentException, InvalidApiException {
		ClientSystem.getSystem().lazyInit();
		return ClientSystem.getSystem().getRpcManager().createProxy(serverAddr, protocol, config);
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
	@NotNull
	public static Proxy getProxy(@NotNull Protocol proxy) throws IllegalArgumentException, SecurityException {
		return ClientSystem.getSystem().getRpcManager().getProxy(proxy);
	}

	/**
	 * Setup global configuration of this Rayson client system.
	 * 
	 * @param config Client system configuration to set.
	 */
	public static void setConfig(@NotNull ClientConfig config) {
		ClientSystem.getSystem().setConfig(config);
	}
}