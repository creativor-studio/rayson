/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

/**
 * Stands for an request line of {@link HttpRequest}.
 * 
 * @author creativor
 */
public interface HttpRequestLine extends HttpStartLine {

	/**
	 * @return HTTP method of this request.
	 */
	HttpMethod getMethod();

	/**
	 * @return The full uri of this request.
	 */
	String getUri();

	/**
	 * @return HTTP version of this request.
	 */
	HttpVersion getVersion();

}