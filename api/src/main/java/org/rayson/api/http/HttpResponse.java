/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import org.rayson.api.Response;

/**
 * Stands for an HTTP response message.
 * 
 * @author creativor
 */
public interface HttpResponse extends HttpMessage, Response {

	@Override
	public HttpStatusLine getStartLine();
}
