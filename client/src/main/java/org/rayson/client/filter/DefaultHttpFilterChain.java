/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.filter;

import org.rayson.api.client.HttpClientFilter;
import org.rayson.api.client.HttpClientRequest;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.client.HttpFilterChain;

/**
 * An default implementation of {@link HttpFilterChain} on client side.
 * 
 * @author Nick Zhang
 *
 */
public class DefaultHttpFilterChain implements HttpFilterChain {
	private HttpClientFilter filter;
	private HttpFilterChain next;

	/**
	 * @param filter HTTP client filter of the new filter chain.
	 */
	public DefaultHttpFilterChain(HttpClientFilter filter) {
		this.filter = filter;
	}

	@Override
	public void doFilter(HttpClientRequest request, HttpClientResponse response) {
		this.filter.doFilter(request, response, next);
	}

	/**
	 * @param next The next chain of current one.
	 */
	public void setNext(HttpFilterChain next) {
		this.next = next;
	}
}