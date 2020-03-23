/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.advanced.server;

import org.rayson.api.Protocol;
import org.rayson.api.server.Server;
import org.rayson.api.server.ServerRequest;
import org.rayson.api.server.ServerResponse;
import org.rayson.api.server.ServiceFilter;
import org.rayson.api.server.ServiceFilterChain;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * @author Nick Zhang
 *
 */
public class ServerLogFilter implements ServiceFilter {
	private static Logger LOG = RaysonLoggerFactory.getLogger(ServerLogFilter.class);

	@Override
	public void doFilter(ServerRequest request, ServerResponse response, ServiceFilterChain chain)
			throws ServiceException {
		LOG.info("Serving service call: " + request);
		chain.doFilter(request, response);
	}

	@Override
	public void init(Server server, Protocol service) throws ServiceException {
		// Do nothing.
	}
}