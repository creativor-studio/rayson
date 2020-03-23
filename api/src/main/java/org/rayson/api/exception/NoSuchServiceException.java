/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.exception;

/**
 * If no such service found in server side.
 * 
 * @author creativor
 */
public class NoSuchServiceException extends RpcException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param serviceName
	 *            Service name which does not exits.
	 */
	public NoSuchServiceException(final String serviceName) {
		super(serviceName);
	}

}
