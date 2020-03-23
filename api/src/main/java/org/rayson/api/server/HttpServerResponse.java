/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import java.nio.charset.Charset;

import org.rayson.api.http.HttpContentType;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpResponse;
import org.rayson.api.http.HttpResponseStatus;

/**
 * Server side {@link HttpResponse} object.
 * 
 * @author creativor
 */
public interface HttpServerResponse extends HttpResponse, HttpMessage {

	/**
	 * Returns the character encoding (MIME charset) used for the body sent in
	 * this response. The charset for the MIME body response can be specified
	 * explicitly or implicitly. The priority order for specifying the response
	 * body is:
	 * <ol>
	 * <li>explicitly per request using {@link #setCharacterEncoding}</li>
	 * <li>UTF-8</li>
	 * </ol>
	 * See RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt) for more information
	 * about character encoding and MIME.
	 *
	 * @return The character encoding, for example, <code>UTF-8</code>
	 */
	public Charset getCharacterEncoding();

	/**
	 * Sets the character encoding (MIME charset) of the response being sent to
	 * the client, for example, to UTF-8. <br>
	 * This method can be called repeatedly to change the character encoding.
	 * 
	 * @param charset a charset specifying only the character set defined by
	 *            IANA Character Sets
	 *            (http://www.iana.org/assignments/character-sets)
	 */
	public void setCharacterEncoding(Charset charset);

	/**
	 * Returns the content type used for the MIME body sent in this response.
	 * The content type proper must have been specified using
	 * {@link #setContentType} before the response is committed.
	 * 
	 * @return Http content type of this response.
	 */
	public HttpContentType getContentType();

	/**
	 * Sets the content type of the response being sent to the client, if the
	 * response has not been committed yet.
	 * <p>
	 * This method may be called repeatedly to change content type and character
	 * encoding. This method has no effect if called after the response has been
	 * committed.
	 * 
	 * @param type a <code>HttpContentType</code> specifying the MIME type of
	 *            the content
	 */
	public void setContentType(HttpContentType type);

	/**
	 * Set business exception of this response with status code and exception
	 * message.
	 * 
	 * @param statusCode Response status code
	 * @param message Error message.
	 */
	public void setException(HttpResponseStatus statusCode, String message);
}
