/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.simple.server;

import org.rayson.server.RaysonServer;

/**
 * Demo server with simple functions. <br>
 * Client testing command is : <br>
 * curl -v
 * "http://127.0.0.1:8080/org.rayson.demo.simple.api.SimpleProtocol/echo?msg=hello"
 * 
 * @author creativor
 */
public class SimpleServer {
	public static void main(final String[] args) throws Exception {
		final RaysonServer server = new RaysonServer(8080);
		server.registerService(new SimpleService());
		server.start();
	}

}
