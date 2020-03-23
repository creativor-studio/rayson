/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.simple.client;

import org.rayson.api.client.RaysonServerAddress;
import org.rayson.client.Rayson;
import org.rayson.demo.simple.api.SimpleProtocol;

/**
 *
 * @author creativor
 */
public class SimpleClient {
	public static void main(final String[] args) {
		try {
			final RaysonServerAddress serverAddr = new RaysonServerAddress("localhost", 8080);
			SimpleProtocol simpleProtocol;

			simpleProtocol = Rayson.createProxy(serverAddr, SimpleProtocol.class);

			System.out.println(new String(simpleProtocol.echo2("Hello World".getBytes())));

		} catch (Throwable e) {
			System.err.println("Failed to invoking demo rpc");
			e.printStackTrace();
		} finally {
			System.exit(0);
			;
		}

	}
}
