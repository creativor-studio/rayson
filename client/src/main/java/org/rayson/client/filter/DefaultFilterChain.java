/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.filter;

import org.rayson.api.client.ClientFilter;
import org.rayson.api.client.ClientFilterChain;
import org.rayson.api.client.ClientRequest;
import org.rayson.api.client.ClientResponse;
import org.rayson.api.exception.ClientFilterException;

/**
 * Default implementation of {@link ClientFilterChain}.
 * 
 * @author Nick Zhang
 *
 */
public class DefaultFilterChain implements ClientFilterChain {
	private ClientFilter filter;
	private ClientFilterChain next;

	/**
	 * @param filter Client filter of the new filter chain.
	 */
	public DefaultFilterChain(ClientFilter filter) {
		this.filter = filter;
	}

	@Override
	public void doFilter(ClientRequest request, ClientResponse response) throws ClientFilterException {
		this.filter.doFilter(request, response, next);
	}

	/**
	 * 
	 * @param next The next chain of current one.
	 */
	public void setNext(ClientFilterChain next) {
		this.next = next;
	}
}