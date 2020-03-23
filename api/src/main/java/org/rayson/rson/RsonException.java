/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

/**
 * It indicates that some exception happened during RSON processing.
 *
 */
public class RsonException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new runtime exception with the specified detail message. The
	 * cause is not initialized, and may subsequently be initialized by a call
	 * to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public RsonException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message and
	 * cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i>
	 * automatically incorporated in this runtime exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by
	 *            the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <code>null</code> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public RsonException(String message, Throwable cause) {
		super(message, cause);
	}

}