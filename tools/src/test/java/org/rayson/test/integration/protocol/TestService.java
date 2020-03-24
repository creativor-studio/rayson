/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration.protocol;

import java.io.IOException;

import org.rayson.api.exception.RpcException;

/**
 * @author creativor
 *
 */
public class TestService implements TestProtocol {

	@Override
	public String concat(String one, int two) throws IOException, RpcException {
		return one.concat(Integer.toString(two));
	}

	@Override
	public String echo(String msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public byte echoByte(byte msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public byte[] echoBytes(byte[] msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public double echoDouble(Double msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public float echoFloat(float msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public Integer echoInt(int msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public long echoLong(long msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public Short echoShort(short msg) throws IOException, RpcException {
		return msg;
	}

	@Override
	public void touch() {
		// Do nothing.
	}

	@Override
	public TestStruct echoBean(TestStruct msg) throws IOException, RpcException {
		return msg;
	}
}