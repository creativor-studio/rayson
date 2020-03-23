/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration;

import org.rayson.api.client.HttpClientFilter;

/**
 * HTTP client filter creating strategy.
 */
interface ClientFilterCreator {
	public HttpClientFilter create(TestStrategy strategy, BaseFlowTest testCase) throws Exception;
}