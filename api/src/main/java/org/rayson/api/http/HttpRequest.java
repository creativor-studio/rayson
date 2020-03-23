/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import org.rayson.api.Request;

/**
 * Stands for an HTTP request message.
 * 
 * @author creativor
 */
public interface HttpRequest extends HttpMessage, Request {

	@Override
	public HttpRequestLine getStartLine();
}
