
/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

import java.nio.charset.Charset;

import org.rayson.api.annotation.ThreadSafe;
import org.rayson.api.serial.ByteArrayInputBuffer;

/**
 * Factory class for creating RSON processing objects. This class provides the
 * most commonly used methods for creating these objects and their corresponding
 * factories. The factory classes provide all the various ways to create these
 * objects.
 * 
 * @author creativor
 */
@ThreadSafe
public class Rson {
	/**
	 * Default provider .
	 */
	private static final RsonProvider PROVIDER = RsonProvider.provider();

	/**
	 * Creates a RSON builder.
	 *
	 * @return a RSON builder.
	 */
	public static RsonBuilder createBuilder() {
		return PROVIDER.createBuilder();
	}

	/**
	 * Creates a JSON parser from a byte array buffer.
	 * 
	 * @param buffer Json byte array source data in buffer.
	 * @param charset The character set which the target {@link RsonElement}
	 *            used.
	 * @return Created parser.
	 */
	public static RsonParser createParser(final ByteArrayInputBuffer buffer, Charset charset) {
		return PROVIDER.createParser(buffer, charset);
	}

	private Rson() {
	}
}