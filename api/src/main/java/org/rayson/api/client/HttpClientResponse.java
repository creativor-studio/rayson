/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.nio.charset.Charset;

import org.rayson.api.http.HttpContentType;
import org.rayson.api.http.HttpResponse;

/**
 * Client side {@link HttpResponse} object.
 * 
 * @author creativor
 *
 */
public interface HttpClientResponse extends HttpResponse {

	/**
	 * Copy this response to target instance.
	 * 
	 * @param target Target response to be copied.
	 * @throws IllegalArgumentException If parameter is <code>null</code>.
	 */
	void copyTo(HttpClientResponse target);

	/**
	 * Returns the name of the character encoding used in the body of this
	 * request. This method returns <code>null</code> if the no character
	 * encoding has been specified.
	 * 
	 * @return a <code>String</code> containing the name of the character
	 *         encoding, or <code>null</code> if the request does not specify a
	 *         character encoding.
	 */
	Charset getCharacterEncoding();

	/**
	 * Returns the content type used for the MIME body sent in this response.
	 * 
	 * @return Http content type of this response.
	 */
	HttpContentType getContentType();

	/**
	 * Set body content of this response.
	 * 
	 * @param body Body content , in byte array.
	 */
	void setBody(byte[] body);

}