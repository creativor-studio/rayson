/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.advanced.server;

import org.rayson.api.http.HttpHeader;
import org.rayson.api.server.HttpFilterChain;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.Server;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * @author Nick Zhang
 *
 */
public class ServerAuthHttpFilter implements HttpServerFilter {
	private static Logger LOG = RaysonLoggerFactory.getLogger(ServerAuthHttpFilter.class);

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain) {
		System.out.println("Recived HTTP request " + request);
		HttpHeader header = request.getHeader("apiKey");
		LOG.info("Got apiKey header: " + header);
		chain.doFilter(request, response);
	}

	@Override
	public void init(Server server) {
		// Do nothing.
	}
}