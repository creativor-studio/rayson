/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.api;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

/**
 *
 * @author creativor
 */
@Service
public interface JwtProtocol extends Protocol {
	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 * @throws IOException
	 */
	public String echo(String msg) throws IOException, RpcException;

}
