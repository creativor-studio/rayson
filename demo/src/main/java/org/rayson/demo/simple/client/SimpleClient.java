/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.simple.client;

import java.io.IOException;

import org.rayson.api.client.RaysonServerAddress;
import org.rayson.api.exception.RpcException;
import org.rayson.client.Rayson;
import org.rayson.demo.simple.api.SimpleProtocol;

/**
 *
 * @author creativor
 */
public class SimpleClient {
	public static void main(final String[] args) throws Exception {
		final RaysonServerAddress serverAddr = new RaysonServerAddress("localhost", 8080);
		SimpleProtocol simpleProtocol = Rayson.createProxy(serverAddr, SimpleProtocol.class);
		try {
			String echoMsg = simpleProtocol.echo("Hello World");
			System.out.println(echoMsg);
		} catch (IOException e) {
			System.err.println("Network error occurred");
		} catch (RpcException e) {
			System.err.println("Invoking RPC got logic error: error_code: " + e.getCode() + " error_message: " + e.getMessage());
		}
	}
}
