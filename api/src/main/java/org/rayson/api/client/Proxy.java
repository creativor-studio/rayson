/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import org.rayson.api.Protocol;
import org.rayson.api.exception.ClientFilterException;
import org.rayson.api.exception.ProtocolException;

/**
 * An proxy is an class implements a specified interface which extends from
 * {@link Protocol} (which called service) , and created at runtime dynamically.
 * <br>
 * When user invoking the method of a proxy instance, the work flow in summary
 * as following:
 * <ul>
 * <li>Intercept the invocation information by reflection.</li>
 * <li>Serialize the invocation into HTTP request.</li>
 * <li>Send the HTTP request to remote server.</li>
 * <li>Wait for the HTTP response.</li>
 * <li>De-serialize the HTTP response to the method result object.</li>
 * <li>Return the result to user.</li>
 * </ul>
 * <h3>Invoking the method of the proxy,may throws the following unchecked
 * exceptions , which can be defined in each method optional.</h3>
 * <ul>
 * <li>{@link ProtocolException} , an unchecked exception indicate that response
 * from server is malformed according to the communicating protocol.</li>
 * <li>{@link ClientFilterException}, unchecked error to indicate that the
 * client side filter itself got error when doing filter job.</li>
 * </ul>
 * 
 * @see ProxyConfig About configure an proxy.
 * @author Nick Zhang
 */
public interface Proxy {
	/**
	 * @return The configuration setting of this proxy.
	 */
	public ProxyConfig getConfig();

	/**
	 * @return The associated protocol class of this proxy.
	 */
	public Class<? extends Protocol> getProtocol();

	/**
	 * @return The remote server address which this proxy will access it's
	 *         service.
	 */
	public RaysonServerAddress getServerAddr();
}