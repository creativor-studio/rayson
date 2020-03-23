/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

import org.rayson.api.server.ServerRequest;

/**
 * Defines an object to provide client request information to a service. The
 * server reates a {@link ServerRequest} object and passes it as an argument to
 * the service's method.
 * 
 * There are two kinds of request in both client side and server side.
 * <ul>
 * <li>Service request.</li>
 * <li>Http request.</li>
 * </ul>
 * 
 * @author Nick Zhang
 */
public interface Request {

}