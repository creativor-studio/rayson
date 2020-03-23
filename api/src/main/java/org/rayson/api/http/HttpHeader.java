/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import java.nio.charset.Charset;

import org.rayson.api.serial.BufferWritable;

/**
 * Stands for an header information in HTTP protocol.
 * 
 * @author creativor
 *
 */
public interface HttpHeader extends BufferWritable {
	/**
	 * Charset of the HTTP header character encoding .ISO-8859-1.
	 */
	public static final Charset CHARSET = Charset.forName("ISO-8859-1");

	/**
	 * @return Name of this header.
	 */
	String getName();

	/**
	 * @return Value of this header.
	 */
	String getValue();

	/**
	 * @return Value of this header,as integer.
	 * @throws NumberFormatException If the value is malformed as integer.
	 */
	int getValueAsInt() throws NumberFormatException;

	/**
	 * @return Value of this header,as long.
	 * @throws NumberFormatException If the value is malformed as long.
	 */
	long getValueAsLong() throws NumberFormatException;
}