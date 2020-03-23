/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.exception;

/**
 * An unchecked error occurred when the Rayson client parsing the response and
 * the response data is not satisfied with the communicating protocol.
 * 
 * @author Nick Zhang
 *
 */
public class ProtocolException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * @param message Message of the new exception.
	 * @param cause Error which cause the new exception.
	 */
	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message Message of the new exception.
	 */
	public ProtocolException(String message) {
		super(message);
	}
}