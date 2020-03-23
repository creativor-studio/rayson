/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration;

import org.rayson.api.Protocol;

/**
 * RPC proxy creating strategy.
 */
interface ProxyCreator<P extends Protocol> {
	public P create(TestStrategy stragy, BaseFlowTest testCase) throws Exception;
}
