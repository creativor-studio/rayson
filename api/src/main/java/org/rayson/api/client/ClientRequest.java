/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.lang.reflect.Method;

import org.rayson.api.Request;

/**
 * RPC request on client side.
 *
 * @author creativor
 */
public interface ClientRequest extends ClientMessage, Request {

	/**
	 * @return Arguments list of this request.
	 */
	Object[] getArguments();

	/**
	 * @return Underling java method associated with this request.
	 */
	Method getMethod();
}