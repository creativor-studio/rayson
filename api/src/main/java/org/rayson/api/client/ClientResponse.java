/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.io.IOException;

import org.rayson.api.Response;
import org.rayson.api.exception.ProtocolException;
import org.rayson.api.exception.RpcException;

/**
 * RPC response on client side.
 */
public interface ClientResponse extends ClientMessage, Response {

	/**
	 * @return Result value of this response.
	 * @throws RpcException If the servicing got error.
	 * @throws IOException If got network error.
	 * @throws ProtocolException If the server side response is wrong format
	 *             according to Rayson protocol .
	 */
	public Object getResult() throws RpcException, IOException, ProtocolException;

	/**
	 * Set up the result value of this response.
	 * 
	 * @param result Result value to be set.
	 */
	public void setResult(Object result);

	/**
	 * Set up the servicing error of this response.
	 * 
	 * @param exception Servicing error of this response.
	 */
	public void setException(RpcException exception);
}