
package org.rayson.api.server.exception;

/**
 * If error occurred when processing an request on Rayson service.
 * 
 * @author Nick Zhang
 *
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new service exception.
	 * 
	 * @param message Error message.
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Construct a new service exception.
	 * 
	 * @param message Error message.
	 * @param cause Exception which cause the new service exception.
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construct a new service exception.
	 * 
	 * @param cause Exception which cause the new service exception.
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}
}