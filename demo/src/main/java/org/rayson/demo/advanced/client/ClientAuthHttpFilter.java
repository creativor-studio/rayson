/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.advanced.client;

import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;

/**
 * @author Nick Zhang
 *
 */
public class ClientAuthHttpFilter implements HttpClientFilter {

	@Override
	public void doFilter(HttpClientRequest request, HttpClientResponse response, HttpFilterChain chain)
			throws ClientFilterException {
		request.addHeader("apiKey", "apiKey-xxxx-xxxx");
		System.out.println("Sending HTTP request " + request + " ...");
		chain.doFilter(request, response);
	}

	@Override
	public void init(Proxy proxy) {
		// Do nothing.
	}
}