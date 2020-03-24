/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration.protocol;

import java.io.IOException;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.SerializedName;
import org.rayson.api.annotation.Service;
import org.rayson.api.exception.RpcException;
import org.rayson.test.integration.SimpleFlowTest;

/**
 * An test {@link Protocol} using in testing of {@link SimpleFlowTest} .
 * 
 * @author creativor
 *
 */
@Service
public interface TestProtocol extends Protocol {
	public String concat(String one, int two) throws IOException, RpcException;

	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 */
	public String echo(@SerializedName("msg") String msg) throws IOException, RpcException;

	public TestStruct echoBean(TestStruct msg) throws IOException, RpcException;

	public byte echoByte(byte msg) throws IOException, RpcException;

	/**
	 * Echo received message.
	 * 
	 * @param msg The message to be echo.
	 * @return Return the given message.
	 */
	public byte[] echoBytes(byte[] msg) throws IOException, RpcException;

	public double echoDouble(Double msg) throws IOException, RpcException;

	public float echoFloat(float msg) throws IOException, RpcException;

	public Integer echoInt(int msg) throws IOException, RpcException;

	public long echoLong(long msg) throws IOException, RpcException;

	public Short echoShort(short msg) throws IOException, RpcException;

	public void touch() throws IOException, RpcException;

}
