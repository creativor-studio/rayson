/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler.rpc;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

/**
 * Protocol used for profiler testing.
 */
@Service
public interface ProfilerProtocol extends Protocol {
	/**
	 * Just response the request.
	 * 
	 * @param req
	 * @return
	 * @throws RpcException
	 * @throws IOException
	 */
	public ProfilerEntity echo(ProfilerEntity req) throws RpcException, IOException;
}
