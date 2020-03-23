/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

/**
 * Stands for an HTTP status line of an {@link HttpResponse}.
 * 
 * @author creativor
 */
public interface HttpStatusLine extends HttpStartLine {
	/**
	 * @return Cloned new instance.
	 */
	HttpStatusLine clone();

	/**
	 * @return The reason phrase according to the response of this status line.
	 */
	String getReasonPhrase();

	/**
	 * @return The status code according to the response of this status line.
	 */
	int getStatusCode();
}