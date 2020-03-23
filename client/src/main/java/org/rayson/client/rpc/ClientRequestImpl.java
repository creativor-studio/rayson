/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import java.lang.reflect.Method;

import org.rayson.api.client.ClientRequest;
import org.rayson.api.serial.JsonStringBuilder;

/**
 * Client request implementation.
 * 
 * @author Nick Zhang
 *
 */
class ClientRequestImpl extends AbstractClientMessage implements ClientRequest {

	private Object[] arguments;

	private Method method;

	public ClientRequestImpl(Method method, Object[] args) {
		super();
		this.method = method;
		this.arguments = args;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public String toString() {
		JsonStringBuilder b = new JsonStringBuilder(this);
		b.append("arguments", arguments);
		b.append("method", method);
		return b.toJson();
	}

}