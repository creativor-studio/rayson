/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

/**
 * HTTP context type supported by Rayson.
 * 
 * @author creativor
 */
public enum HttpContentType {
	/**
	 * Standard HTTP 1.1 Form date encoding.
	 */
	FORM_URLENCODED("application/x-www-form-urlencoded"),
	/**
	 * JSON format content type.
	 */
	JSON("application/json"),
	/**
	 * For rayson RPC.
	 */
	RSON("application/rpc.rayson");

	/**
	 * Find a type by giving an type name.
	 * 
	 * @param type Type name string.
	 * @return Type which type name matched the giving type name ignore case. Or
	 *         else, {@link #JSON} returns.
	 * @throws IllegalArgumentException If parameter is <code>null</code>.
	 * 
	 */
	public static HttpContentType typeOf(String type) {
		if (type == null)
			throw new IllegalArgumentException("type should not be null");

		type = type.trim();

		if (type.isEmpty())
			return HttpContentType.JSON;

		for (HttpContentType v : values()) {
			if (type.equalsIgnoreCase(v.typeString))
				return v;
		}

		return JSON;
	}

	private String typeString;

	private HttpContentType(final String typeString) {
		this.typeString = typeString;
	}

	/**
	 * @return Type string .
	 */
	public String typeName() {
		return typeString;
	}
}