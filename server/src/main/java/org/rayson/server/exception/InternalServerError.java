
package org.rayson.server.exception;

/**
 * An internal error occurred inside the server .
 * 
 * @author creativor
 *
 */
public class InternalServerError extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * @param message Error message.
	 */
	public InternalServerError(String message) {
		super(message);
	}

	/**
	 * @param message Error message.
	 * @param cause Exception which cause this new error.
	 */
	public InternalServerError(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause Exception which cause this new error.
	 */
	public InternalServerError(Throwable cause) {
		super(cause);
	}
}