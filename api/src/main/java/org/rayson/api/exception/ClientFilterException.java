/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.exception;

/**
 * Client side filter itself got error.
 * 
 * @author Nick Zhang
 *
 */
public class ClientFilterException extends Error {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new exception with error message.
	 * 
	 * @param message Error message.
	 */
	public ClientFilterException(final String message) {
		super(message);
	}

	/**
	 * Construct a new exception with error message and error cause.
	 * 
	 * @param message Error message.
	 * @param cause Error which cause this error.
	 */
	public ClientFilterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construct a new exception with error cause.
	 * 
	 * @param cause Error which cause this error.
	 */
	public ClientFilterException(final Throwable cause) {
		super(cause);
	}
}