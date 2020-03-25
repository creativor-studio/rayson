/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.exception;

import java.lang.reflect.Method;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;

/**
 * If the according protocol {@link Protocol} is an invalid API for Rayson
 * service.
 */
public class InvalidApiException extends Exception {

	private static final long serialVersionUID = 1;
	private Method method;

	/**
	 * Construct a new exception.
	 * 
	 * @param method Source Java method which cause this error.
	 * @param message Error message.
	 */
	public InvalidApiException(@NotNull Method method, String message) {
		super(message);
		this.method = method;
	}

	/**
	 * @return The source method which is not an validate one for Rayson API.
	 */
	public Method getMethod() {
		return method;
	}
}