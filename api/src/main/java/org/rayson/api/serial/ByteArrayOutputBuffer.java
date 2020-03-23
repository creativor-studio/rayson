/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.serial;

import org.rayson.api.annotation.NotNull;

/**
 * An buffer used to write byte array content.
 * 
 * @author creativor
 */
public interface ByteArrayOutputBuffer {

	/**
	 * Write a double value to giving <code>buffer</code>.
	 * 
	 * @param buffer Target data source buffer.
	 * @param value Value to be written to target buffer.
	 */
	public static void writeDouble(@NotNull final ByteArrayOutputBuffer buffer, final double value) {

		final long l = Double.doubleToLongBits(value);
		buffer.write((byte) (l >>> 56));
		buffer.write((byte) (l >>> 48));
		buffer.write((byte) (l >>> 40));
		buffer.write((byte) (l >>> 32));
		buffer.write((byte) (l >>> 24));
		buffer.write((byte) (l >>> 16));
		buffer.write((byte) (l >>> 8));
		buffer.write((byte) (l >>> 0));

	}

	/**
	 * Write a float value to giving <code>buffer</code>.
	 * 
	 * @param buffer Target data source buffer.
	 * @param value Value to be written to target buffer.
	 */
	public static void writeFloat(@NotNull final ByteArrayOutputBuffer buffer, final float value) {

		final int i = Float.floatToIntBits(value);
		buffer.write(i >>> 24 & 0xFF);
		buffer.write(i >>> 16 & 0xFF);
		buffer.write(i >>> 8 & 0xFF);
		buffer.write(i >>> 0 & 0xFF);

	}

	/**
	 * Write a integer value to giving <code>buffer</code>.
	 * 
	 * @param buffer Target data source buffer.
	 * @param value Value to be written to target buffer.
	 */
	public static void writeInt(@NotNull final ByteArrayOutputBuffer buffer, final int value) {

		buffer.write(value >>> 24 & 0xFF);
		buffer.write(value >>> 16 & 0xFF);
		buffer.write(value >>> 8 & 0xFF);
		buffer.write(value >>> 0 & 0xFF);

	}

	/**
	 * Write a long value to giving <code>buffer</code>.
	 * 
	 * @param buffer Target data source buffer.
	 * @param value Value to be written to target buffer.
	 */
	public static void writeLong(@NotNull final ByteArrayOutputBuffer buffer, final long value) {

		buffer.write((byte) (value >>> 56));
		buffer.write((byte) (value >>> 48));
		buffer.write((byte) (value >>> 40));
		buffer.write((byte) (value >>> 32));
		buffer.write((byte) (value >>> 24));
		buffer.write((byte) (value >>> 16));
		buffer.write((byte) (value >>> 8));
		buffer.write((byte) (value >>> 0));

	}

	/**
	 * Write a short value to giving <code>buffer</code>.
	 * 
	 * @param buffer Target data source buffer.
	 * @param value Value to be written to target buffer.
	 */
	public static void writeShort(@NotNull final ByteArrayOutputBuffer buffer, final short value) {

		buffer.write(value >>> 8 & 0xFF);
		buffer.write(value >>> 0 & 0xFF);

	}

	/**
	 * Resets the <code>count</code> field of this byte array output stream to
	 * zero, so that all currently accumulated output in the buffer is
	 * discarded. The buffer can be used again, reusing the already allocated
	 * buffer space.
	 * 
	 */
	public void reset();

	/**
	 * Returns the current size of the buffer.
	 * 
	 * @return the value of the <code>count</code> field, which is the number of
	 *         valid bytes in this buffer.
	 */
	public int size();

	/**
	 * Creates a newly allocated byte array. Its size is the current size of
	 * this buffer and the valid contents of the buffer have been copied into
	 * it.
	 * 
	 * @return the current contents of this buffer, as a byte array.
	 * @see java.io.ByteArrayOutputStream#size()
	 */
	public byte[] toByteArray();

	/**
	 * Converts the buffer's contents into a string decoding bytes using the
	 * platform's default character set. The length of the new <code>String</code>
	 * is a function of the character set, and hence may not be equal to the
	 * size of the buffer.
	 * 
	 * <p>
	 * This method always replaces malformed-input and unmappable-character
	 * sequences with the default replacement string for the platform's default
	 * character set. The {@linkplain java.nio.charset.CharsetDecoder} class
	 * should be used when more control over the decoding process is required.
	 * 
	 * @return String decoded from the buffer's contents.
	 * @since JDK1.1
	 */
	@Override
	public String toString();

	/**
	 * Writes the specified byte array to this byte array buffer.
	 * 
	 * @param b the data.
	 */
	public void write(final byte[] b);

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * offset <code>off</code> to this byte array buffer.
	 * 
	 * @param b the data.
	 * @param off the start offset in the data.
	 * @param len the number of bytes to write.
	 */
	public void write(final byte[] b, final int off, final int len);

	/**
	 * Writes the specified byte to this byte array buffer.
	 * 
	 * @param b the byte to be written.
	 */
	public void write(final int b);
}