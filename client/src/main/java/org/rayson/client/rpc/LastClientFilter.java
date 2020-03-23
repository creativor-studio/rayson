/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import org.rayson.api.client.ClientFilter;
import org.rayson.api.client.ClientFilterChain;
import org.rayson.api.client.ClientRequest;
import org.rayson.api.client.ClientResponse;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.client.http.HttpClientResponseImpl;

/**
 * Last {@link ClientFilterChain} on the associated filter chain.
 * 
 * @author Nick Zhang
 *
 */
class LastClientFilter implements ClientFilter {
	private RpcProxy proxy;

	LastClientFilter(RpcProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void doFilter(ClientRequest request, ClientResponse response, ClientFilterChain chain) throws ClientFilterException {

		ClientResponseImpl respImpl = (ClientResponseImpl) response;

		// Setup http request .

		HttpClientRequest httpRequest = proxy.creatHttpRequest(request);

		HttpClientResponseImpl httpResponse = new HttpClientResponseImpl();

		proxy.getFilterManager().doFilter(httpRequest, httpResponse);

		// Setup client response from http response.

		respImpl.setup(httpResponse);

	}

	@Override
	public void init(Proxy proxy) {
		// Do nothing.
	}
}