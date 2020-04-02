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
import org.rayson.api.server.RpcContext;
import org.rayson.api.server.Server;

/**
 * @author Nick Zhang
 *
 */
public class ServerAuthHttpFilter implements HttpServerFilter {

	@Override
	public void doFilter(HttpServerRequest request, HttpServerResponse response, HttpFilterChain chain) {
		System.out.println("Recived HTTP request " + request);
		HttpHeader header = request.getHeader("apiKey");
		// Get user id from apiKey header.
		String userId = getUserId(header);
		RpcContext.getContext().setAttribute("userId", userId);
		chain.doFilter(request, response);
	}

	private String getUserId(HttpHeader header) {
		// For demo only.
		return header.getValue();
	}

	@Override
	public void init(Server server) {
		// Do nothing.
	}
}