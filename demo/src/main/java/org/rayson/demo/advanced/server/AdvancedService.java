/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.advanced.server;

import java.io.IOException;

import org.rayson.api.annotation.ServiceConfig;
import org.rayson.api.exception.RpcException;
import org.rayson.api.server.RpcContext;
import org.rayson.demo.advanced.api.AdvancedProtocol;
import org.rayson.demo.advanced.api.SerialObject;
import org.rayson.rson.element.ObjectElement;

/**
 *
 * @author creativor
 */
@ServiceConfig(filters = { ServerLogFilter.class })
public class AdvancedService implements AdvancedProtocol {

	@Override
	public String echo(final String msg) throws IOException {
		String userId = (String) RpcContext.getContext().getAttribute("userId");
		return msg;
	}

	@Override
	public SerialObject echo2(SerialObject msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public ObjectElement echoRson(ObjectElement msg) throws IOException, RpcException {
		return msg;
	}

}
