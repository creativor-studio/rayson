/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.client;

import org.rayson.api.client.ProxyConfig;
import org.rayson.api.client.RaysonServerAddress;
import org.rayson.client.Rayson;
import org.rayson.demo.jwt.api.JwtProtocol;

/**
 *
 * @author creativor
 */
public class JwtClient {

	public static void main(final String[] args) throws Exception {
		final RaysonServerAddress serverAddr = new RaysonServerAddress("localhost", 8080);
		ProxyConfig config = new ProxyConfig();
		config.addHttpFilter(ClientAuthHttpFilter.class);
		JwtProtocol testProtocol;
		testProtocol = Rayson.createProxy(serverAddr, JwtProtocol.class, config);
		System.out.println(testProtocol.echo("Hello World"));

	}
}
