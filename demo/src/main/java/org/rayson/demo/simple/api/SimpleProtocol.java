/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.simple.api;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

/**
 *
 * @author creativor
 */
@Service
public interface SimpleProtocol extends Protocol {
	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 * @throws IOException
	 */
	@TestAnnotation("test annotaiton")
	public String echo(String msg) throws IOException, RpcException;

	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 * @throws IOException
	 */
	public byte[] echo2(byte[] msg) throws IOException, RpcException;
}
