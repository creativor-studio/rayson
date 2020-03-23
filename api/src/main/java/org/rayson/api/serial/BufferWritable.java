/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.serial;

import java.nio.charset.Charset;

/**
 * Indicate that one object can be write to an {@link ByteArrayOutputBuffer}.
 * 
 * @author creativor
 */
public interface BufferWritable {
	/**
	 * Write this object to byte array buffer.
	 * 
	 * @param charset Character encoding of the target byte array data data.
	 * @param buffer Byte array buffer to write this object.
	 */
	public void writeTo(final ByteArrayOutputBuffer buffer, Charset charset);
}
