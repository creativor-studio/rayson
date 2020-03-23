/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.client;

import java.nio.charset.Charset;

import org.rayson.api.http.HttpRequest;

/**
 * HTTP request on client side.
 * 
 * @author creativor
 *
 */
public interface HttpClientRequest extends HttpRequest {

	/**
	 * Returns the character encoding (MIME charset) used for the body sent in
	 * this response.
	 *
	 * @return The character encoding, for example, <code>UTF-8</code>
	 */
	Charset getCharacterEncoding();

	/**
	 * @return The unique id of this request.
	 */
	long getId();

	/**
	 * Set body content of this HTTP request.
	 * 
	 * @param body Body content, in byte array.
	 */
	void setBody(byte[] body);

	/**
	 * Sets the character encoding (MIME charset) of the request being sent to
	 * the server, for example, to UTF-8. <br>
	 * This method can be called repeatedly to change the character encoding.
	 * 
	 * @param charset a charset specifying only the character set defined by
	 *            IANA Character Sets
	 *            (http://www.iana.org/assignments/character-sets)
	 * @throws IllegalArgumentException If parameter is <code>null</code>.
	 * 
	 */
	void setCharacterEncoding(Charset charset) throws IllegalArgumentException;

}