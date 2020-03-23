/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.util.Collection;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.struct.KeyValues;

/**
 * RPC message on client side.
 *
 * @author creativor
 */
public interface ClientMessage {
	/**
	 * Add header to this request.
	 * 
	 * @param name Header key name.
	 * @param value Header value.
	 */
	public void addHeader(@NotNull String name, @NotNull String value);

	/**
	 * @return Returns all the HTTP headers of this request.Unmodifiable.
	 */
	public Collection<KeyValues> getAllHeaders();

	/**
	 * Returns all the values of the specified request header.
	 * <p>
	 * Some headers, such as <code>Accept-Language</code> can be sent by clients
	 * as several headers each with a different value rather than sending the
	 * header as a comma separated list.
	 * <p>
	 * If the request did not include any headers of the specified name, this
	 * method returns an <code>null</code>. The header name is case insensitive.
	 * You can use this method with any request header.
	 *
	 * @param name a <code>String</code> specifying the header name
	 * @return an <code>{@link KeyValues}</code> containing the values of the
	 *         requested header. If the request does not have any headers of
	 *         that name return an null.
	 */
	public KeyValues getHeaders(@NotNull String name);
}