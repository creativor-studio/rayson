/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import java.util.Collection;

import org.rayson.api.Request;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.protocol.mirror.MethodMirror;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.struct.KeyValues;
import org.rayson.api.struct.RpcParameter;
import org.rayson.api.struct.RpcParameters;

/**
 * RPC request on server side.
 * 
 * @author Nick Zhang
 */
public interface ServerRequest extends Request {
	/**
	 * @return All the HTTP headers of this request.
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

	/**
	 * @return {@link HttpServerRequest} associated with this server request.
	 */
	public HttpServerRequest getHttpRequest();

	/**
	 * @return The Java method mirror associated to this request.
	 */
	public MethodMirror getMethodMirror();

	/**
	 * Returns the value of a request parameter as a <code>String</code>, or
	 * <code>null</code> if the parameter does not exist. Request parameters are
	 * extra information sent with the request. For HTTP servlets, parameters
	 * 
	 * @param name a <code>KeyValue</code> specifying the name of the parameter
	 * @return a <code>String</code> representing the single value of the
	 *         parameter
	 * @see #getParameters()
	 */
	public RpcParameter getParameter(String name);

	/**
	 * @return All the parameters of this request.
	 */
	public RpcParameters getParameters();

	/**
	 * @return The protocol mirror associated to this request.
	 */
	public ProtocolMirror getProtocolMirror();
}