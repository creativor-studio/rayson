/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.client;

import java.net.UnknownHostException;

import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.client.Rayson;
import org.rayson.demo.jwt.api.JwtProtocol;

/**
 *
 * @author creativor
 */
public class JwtClient {

	public static void main(final String[] args) throws IllegalArgumentException, UnknownHostException {

		final RaysonServerAddress serverAddr = new RaysonServerAddress("localhost", 8080);
		ProxyConfig config = new ProxyConfig();
		config.addHttpFilter(ClientAuthHttpFilter.class);
		JwtProtocol testProtocol;
		try {
			testProtocol = Rayson.createProxy(serverAddr, JwtProtocol.class, config);

			System.out.println(testProtocol.echo("Hello World"));

		} catch (Throwable e) {
			System.err.println("Failed to invoking demo rpc");
			e.printStackTrace();
		}

		System.exit(0);

	}
}
