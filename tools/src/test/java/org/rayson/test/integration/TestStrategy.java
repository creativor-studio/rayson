/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import org.rayson.api.client.HttpClientFilter;

/**
 * An strategy definition used in testing {@link BaseFlowTest} test methods.
 * 
 */
@Documented
@Retention(RUNTIME)
public @interface TestStrategy {
	/**
	 * Define HTTP client filter strategy.
	 */
	public Class<? extends HttpClientFilter> clientFilter() default HttpClientFilter.class;

	/**
	 * Define RPC proxy creating strategy.
	 */
	public Class<? extends ProxyCreator> proxyCreator() default ProxyCreator.class;

	/**
	 * Define server creating strategy.
	 */
	public Class<? extends ServerCreator> serverCreator() default ServerCreator.class;

	/**
	 * Whether using SSL protocol.
	 */
	public boolean usingSsl() default false;
}
