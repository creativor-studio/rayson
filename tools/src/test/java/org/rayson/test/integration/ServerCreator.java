/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration;

import org.rayson.server.RaysonServer;

/**
 * Server creating strategy.
 */
interface ServerCreator {
	public RaysonServer create(TestStrategy strategy, BaseFlowTest testCase) throws Exception;
}