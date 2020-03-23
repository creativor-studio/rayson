/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.exception;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.http.HttpResponseStatus;

/**
 * When invoking an RPC and got business error.
 * 
 * @author creativor
 */
public class RpcException extends Exception {
	/**
	 * An error code it means that no error code specified.
	 */
	public final static int UNSPECIFIC_ERROR_CODE = 500;
	private static final long serialVersionUID = 1L;

	private int code;
	private final String message;

	/**
	 * Construct a new instance with given error status.
	 * 
	 * @param status Response status which contains error code and reason
	 *            phrase.
	 */
	public RpcException(@NotNull final HttpResponseStatus status) {
		this(status.getCode(), status.getReasonPhrase());
	}

	/**
	 * Construct a new exception.
	 * 
	 * @param code Error code.
	 * @param message Error message.
	 */
	public RpcException(final int code, final String message) {
		super();
		this.code = code;
		this.message = message;
	}

	/**
	 * Construct a new exception.
	 * 
	 * @param message Error message.
	 */
	public RpcException(@NotNull final String message) {
		code = UNSPECIFIC_ERROR_CODE;
		this.message = message;
	}

	/**
	 * @return Error code of this exception.
	 */
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Set error code of this exception.
	 * 
	 * @param code Error code number according to this exception.
	 */
	protected void setCode(final int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("code:" + code);
		sb.append(", message:" + message);
		sb.append("}");
		return sb.toString();
	}
}