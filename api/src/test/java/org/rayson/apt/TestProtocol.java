/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.apt;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.SerializedName;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;

@Service
public interface TestProtocol extends Protocol {
	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 */
	public String echo(@SerializedName("msg") String msg) throws IOException, RpcException;

	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 */
	public byte[] echo2(byte[] msg) throws IOException, RpcException;
}
