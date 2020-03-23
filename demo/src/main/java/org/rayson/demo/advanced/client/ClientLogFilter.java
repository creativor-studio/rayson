/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.advanced.client;

import org.rayson.api.client.ClientFilter;
import org.rayson.api.client.ClientFilterChain;
import org.rayson.api.client.ClientRequest;
import org.rayson.api.client.ClientResponse;
import org.rayson.api.client.Proxy;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * @author Nick Zhang
 *
 */
public class ClientLogFilter implements ClientFilter {

	private static Logger LOG = RaysonLoggerFactory.getLogger(ClientLogFilter.class);

	@Override
	public void doFilter(ClientRequest request, ClientResponse response, ClientFilterChain chain)
			throws ClientFilterException {
		LOG.info("Invoking service call: " + request);
		chain.doFilter(request, response);
	}

	@Override
	public void init(Proxy proxy) {
		// Do nothing.
	}
}