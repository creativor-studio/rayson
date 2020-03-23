/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.simple.server;

import java.io.IOException;

import org.rayson.api.annotation.ServiceConfig;
import org.rayson.api.exception.RpcException;
import org.rayson.demo.simple.api.SimpleProtocol;

/**
 *
 * @author creativor
 */
@ServiceConfig()
public class SimpleService implements SimpleProtocol {

	@Override
	public String echo(final String msg) throws IOException {
		return msg;
	}

	@Override
	public byte[] echo2(byte[] msg) throws IOException, RpcException {
		return msg;
	}
}
