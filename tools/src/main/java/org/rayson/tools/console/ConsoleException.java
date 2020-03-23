/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.console;

/**
 * Indicate that some exception in {@link RConsole} occurred.
 * 
 * @author creativor
 */
public class ConsoleException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConsoleException(final String message) {
		super(message);
	}

	public ConsoleException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
