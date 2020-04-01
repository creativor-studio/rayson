/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.jwt.server;

import org.rayson.server.RaysonServer;

/**
 * Demo server with advanced features, Such as filters on both server and client
 * sides. <br>
 * Client testing command is : <br>
 * curl -v
 * "http://127.0.0.1:8080/org.rayson.demo.server.TestProtocol/echo?msg=hello"
 * 
 * @author creativor
 */
public class JwtServer {
	public static void main(final String[] args) throws Exception {
		final RaysonServer server = new RaysonServer(8080);
		server.registerService(new JwtService());
		server.addHttpFilter(ServerAuthHttpFilter.class);
		server.start();
	}

}
