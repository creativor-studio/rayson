package org.rayson.demo.mybuddy;

import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.server.RaysonServer;
import org.rayson.server.exception.InternalServerError;

/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

/**
 * @author Nick Zhang
 *
 */
public class MyBuddy {
	public static void main(String[] args) throws IBuddyException, IllegalArgumentException, DuplicateServiceException,
			ServiceException, InvalidApiException, InternalServerError {
		RaysonServer server = new RaysonServer(8666);
		System.out.println("Starting MyBuddy service on " + server.getName() + " ...");
		server.start();
		server.registerService(new MyBuddyService());
	}
}
