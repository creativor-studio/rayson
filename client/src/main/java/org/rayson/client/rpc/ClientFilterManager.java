/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.rpc;

import java.util.List;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.client.ClientFilter;
import org.rayson.api.client.ClientFilterChain;
import org.rayson.api.client.ClientRequest;
import org.rayson.api.client.ClientResponse;
import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.client.filter.DefaultFilterChain;
import org.rayson.client.filter.DefaultHttpFilterChain;
import org.rayson.client.http.LastHttpClientFilter;
import org.rayson.share.util.ReflectionTool;

/**
 * Filter manager on Rayson client side.
 * 
 * @author Nick Zhang
 *
 */
class ClientFilterManager {

	private ClientFilterChain firstChain;
	private HttpFilterChain firstHttpChain;

	private ClientFilterChain lastChain;
	private HttpFilterChain lastHttpChain;
	private RpcProxy proxy;

	ClientFilterManager(RpcProxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * Add global client filters to this filter manager.<br>
	 * 
	 * @param filters Filter type list.
	 * @throws IllegalArgumentException If failed to add the filter.
	 */
	void addFilter(List<Class<? extends ClientFilter>> filters) throws IllegalArgumentException {
		DefaultFilterChain chain;
		DefaultFilterChain previousChain = null;
		ClientFilter filter;
		if (filters != null) {
			for (Class<? extends ClientFilter> filterType : filters) {
				try {
					filter = ReflectionTool.newInstance(filterType);
					filter.init(proxy);
				} catch (Exception e) {
					throw new IllegalArgumentException("Failed to initialize client filter" + filterType, e);
				}
				chain = new DefaultFilterChain(filter);
				if (previousChain != null) {
					previousChain.setNext(chain);
				}
				previousChain = chain;
				if (firstChain == null) {
					firstChain = chain;
				}
			}
		}

		// Make default last client filter.
		lastChain = new DefaultFilterChain(new LastClientFilter(proxy));
		if (previousChain != null) {
			previousChain.setNext(lastChain);
		}
		if (firstChain == null) {
			firstChain = lastChain;
		}
	}

	/**
	 * Add HTTP filters to this filter manager.<br>
	 * 
	 * @param filters Filter type list.
	 * @throws IllegalArgumentException If failed to add the filter.
	 */
	void addHttpFilter(List<Class<? extends HttpClientFilter>> filters) throws IllegalArgumentException {
		DefaultHttpFilterChain chain;
		DefaultHttpFilterChain previousChain = null;
		HttpClientFilter filter;
		if (filters != null) {
			for (Class<? extends HttpClientFilter> filterType : filters) {
				try {
					filter = ReflectionTool.newInstance(filterType);
					filter.init(proxy);
				} catch (Exception e) {
					throw new IllegalArgumentException("Failed to initialize client filter" + filterType, e);
				}
				chain = new DefaultHttpFilterChain(filter);
				if (previousChain != null) {
					previousChain.setNext(chain);
				}
				previousChain = chain;
				if (firstHttpChain == null) {
					firstHttpChain = chain;
				}
			}
		}

		// Make default last client filter.
		lastHttpChain = new DefaultHttpFilterChain(new LastHttpClientFilter(proxy));
		if (previousChain != null) {
			previousChain.setNext(lastHttpChain);
		}
		if (firstHttpChain == null) {
			firstHttpChain = lastHttpChain;
		}
	}

	/**
	 * Do filter an request
	 * 
	 * @param request RPC request to be filtered.
	 * @param response RPC response to be filtered.
	 * @throws ClientFilterException If error occurred when do filtering job.
	 */
	public void doFilter(@NotNull final ClientRequest request, @NotNull final ClientResponse response) throws ClientFilterException {

		firstChain.doFilter(request, response);

	}

	/**
	 * Do filter an request
	 * 
	 * @param httpRequest HTTP request to be filtered.
	 * @param httpResponse HTTP response to be filtered.
	 * @throws ClientFilterException If error occurred when do filtering job.
	 */
	public void doFilter(HttpClientRequest httpRequest, HttpClientResponse httpResponse) throws ClientFilterException {

		firstHttpChain.doFilter(httpRequest, httpResponse);

	}
}