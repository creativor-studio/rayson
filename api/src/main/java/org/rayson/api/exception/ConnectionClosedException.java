/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.exception;

import java.io.IOException;

/**
 * If the socket connection is closed.
 * 
 * @author creativor
 */
public class ConnectionClosedException extends IOException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct exception instance with error message.
	 * 
	 * @param message Error message.
	 */
	public ConnectionClosedException(final String message) {
		super(message);
	}

	/**
	 * Construct exception instance with error message and error cause.
	 * 
	 * @param message Error message.
	 * @param cause Error which cause this error.
	 */
	public ConnectionClosedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
