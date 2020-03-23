/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.demo.stock.server;

import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.server.ServerConfig;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.server.RaysonServer;
import org.rayson.server.ServerConfigImpl;
import org.rayson.server.exception.InternalServerError;
import org.rayson.share.config.ConfigBase;

/**
 * 
 * @author creativor
 */
public class StockServer {
	public static void main(final String[] args)
			throws DuplicateServiceException, ServiceException,
			InternalServerError, IllegalArgumentException, InvalidApiException {
		ServerConfig config = ConfigBase.newDefault(ServerConfigImpl.class);
		// config.setValue(ConfigKey.SSL_ENABLED, true);
		// config.setValue(ConfigKey.SSL_KEYSTORE_PASSWORD, "creativor");
		final RaysonServer server = new RaysonServer(8888, config);
		server.start();
		server.registerService(new StockServiceImpl());
	}

}
