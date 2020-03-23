/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.client.http;

import java.io.IOException;

import org.rayson.api.annotation.Nullable;
import org.rayson.api.client.HttpClientResponse;
import org.rayson.api.exception.ProtocolException;
import org.rayson.share.client.HttpClientResponseBase;

/**
 * Implementation of {@link HttpClientResponse}.
 * 
 * @author creativor
 */
public class HttpClientResponseImpl extends HttpClientResponseBase {

	private Throwable error = null;

	/**
	 * Setup the error occurred when sending the request or receiving or parsing
	 * the response.<br>
	 * Typically, the error should be one kind of the following:
	 * <ul>
	 * <li>{@link IOException}</li>
	 * <li>{@link ProtocolException}</li>
	 * <li>{@link IOException}</li>
	 * </ul>
	 * 
	 * @param error Error to be set.
	 */
	public void setError(Throwable error) {
		this.error = error;
	}

	/**
	 * @return The error occurred when sending the request or receiving or
	 *         parsing the response.<code>null</code> means no such error
	 *         occurred.
	 */
	@Nullable
	public Throwable getError() {
		return error;
	}
}
