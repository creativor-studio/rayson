/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.advanced.server;

import java.io.IOException;

import org.rayson.api.annotation.ServiceConfig;
import org.rayson.api.exception.RpcException;
import org.rayson.demo.advanced.api.AdvancedProtocol;
import org.rayson.demo.advanced.api.SerialObject;

/**
 *
 * @author creativor
 */
@ServiceConfig(filters = { ServerLogFilter.class })
public class AdvancedService implements AdvancedProtocol {

	@Override
	public String echo(final String msg) throws IOException {
		return msg;
	}

	@Override
	public SerialObject echo2(SerialObject msg) throws IOException, RpcException {
		return msg;
	}

}
