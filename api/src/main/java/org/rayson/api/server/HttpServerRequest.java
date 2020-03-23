/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server;

import org.rayson.api.annotation.Nullable;
import org.rayson.api.http.HttpMessage;
import org.rayson.api.http.HttpRequest;

/**
 * HTTP request on server side.
 * 
 * @author creativor
 */
public interface HttpServerRequest extends HttpRequest, HttpMessage {
	/**
	 * Returns the name of the character encoding used in the body of this
	 * request. This method returns <code>null</code> if the no character
	 * encoding has been specified.
	 * 
	 * @return a <code>String</code> containing the name of the character
	 *         encoding, or <code>null</code> if the request does not specify a
	 *         character encoding.
	 */
	public String getCharacterEncoding();

	/**
	 * Returns the MIME type of the body of the request, or <code>null</code> if
	 * the type is not known. Different from servlet request, it not including
	 * character encoding setting string.
	 * 
	 * @return a <code>String</code> containing the name of the MIME type of the
	 *         request, or null if the type is not known
	 */
	public String getContentType();

	/**
	 * @return The request URL. Excludes query string.
	 */
	public String getUrl();

	/**
	 * @return The request id send from client.<code>null</code> means no such
	 *         id found yet.
	 */
	@Nullable("No request id specifed")
	public String getRequestId();

	/**
	 * @return Remote ip address which this request send from.
	 */
	public abstract String getRemoteAddr();

	/**
	 * @return Underling local socket address's port which received this request
	 *         from.
	 */
	public abstract int getLocalPort();

	/**
	 * @return Remote socket address's port number which this request send from.
	 */
	public abstract int getRemotePort();
}