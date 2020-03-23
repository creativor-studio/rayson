/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.server.exception;

/**
 * If the Rayson service is exists already.
 * 
 * @author creativor
 */
public class DuplicateServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct exception instance with error message.
	 * 
	 * @param message Error message.
	 */
	public DuplicateServiceException(final String message) {
		super(message);
	}

	/**
	 * Construct exception instance with error message and error cause.
	 * 
	 * @param message Error message.
	 * @param cause Error which cause this error.
	 */
	public DuplicateServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

}