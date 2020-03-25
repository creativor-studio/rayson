/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

import org.rayson.api.server.ServerResponse;

/**
 * Defines an object to assist a service in sending a response to the client.
 * The server creates a {@link ServerResponse} object and passes it as an
 * argument to the service's service method.<br>
 * There are two kinds of request in both client side and server side.
 * <ul>
 * <li>Service response.</li>
 * <li>Http response.</li>
 * </ul>
 * 
 * @author Nick Zhang
 */
public interface Response {

}
