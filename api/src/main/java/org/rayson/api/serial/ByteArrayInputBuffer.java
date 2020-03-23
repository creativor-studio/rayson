/**
 * Copyright © 2020 Creativor Studio. About license information, please see
 * LICENSE.txt.
 */

package org.rayson.api.serial;

import java.io.EOFException;

import org.rayson.api.annotation.NotNull;

/**
 * An buffer used to read byte array content.
 * 
 * @author creativor
 */
public interface ByteArrayInputBuffer {
	/**
	 * Read a double value from giving <code>buffer</code>.
	 * 
	 * @param buffer Data source buffer.
	 * @return Double number result.
	 * @exception EOFException If this buffer has reached the end.
	 */
	public static double readDouble(@NotNull final ByteArrayInputBuffer buffer) throws EOFException {
		return Double.longBitsToDouble(readLong(buffer));
	}

	/**
	 * Read a float value from giving <code>buffer</code>.
	 * 
	 * @param buffer Data source buffer.
	 * @return Float number result.
	 * @exception EOFException If this buffer has reached the end.
	 */
	public static float readFloat(@NotNull final ByteArrayInputBuffer buffer) throws EOFException {
		return Float.intBitsToFloat(readInt(buffer));
	}

	/**
	 * Read a integer value from giving <code>buffer</code>.
	 * 
	 * @param buffer Data source buffer.
	 * @return Integer number result.
	 * @exception EOFException If this buffer has reached the end.
	 */
	public static int readInt(@NotNull final ByteArrayInputBuffer buffer) throws EOFException {
		final int ch1 = buffer.read();
		final int ch2 = buffer.read();
		final int ch3 = buffer.read();
		final int ch4 = buffer.read();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/**
	 * Read a long value from giving <code>buffer</code>.
	 * 
	 * @param buffer Data source buffer.
	 * @return Long number result.
	 * 
	 * @exception EOFException If this buffer has reached the end.
	 */
	public static long readLong(@NotNull final ByteArrayInputBuffer buffer) throws EOFException {
		final byte[] b = new byte[8];
		buffer.read(b, 0, 8);
		return (((long) b[0] << 56) + ((long) (b[1] & 255) << 48) + ((long) (b[2] & 255) << 40) + ((long) (b[3] & 255) << 32) + ((long) (b[4] & 255) << 24)
				+ ((b[5] & 255) << 16) + ((b[6] & 255) << 8) + ((b[7] & 255) << 0));
	}

	/**
	 * Read a short value from giving <code>buffer</code>.
	 * 
	 * @param buffer Data source buffer.
	 * @exception EOFException If this buffer has reached the end.
	 * @return Short number result.
	 */
	public static short readShort(@NotNull final ByteArrayInputBuffer buffer) throws EOFException {
		final int ch1 = buffer.read();
		final int ch2 = buffer.read();
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	/**
	 * Returns the number of remaining bytes that can be read (or skipped over)
	 * from this input stream.
	 * <p>
	 * The value returned is <code>count&nbsp;- pos</code>, which is the number
	 * of bytes remaining to be read from the input buffer.
	 *
	 * @return the number of remaining bytes that can be read (or skipped over)
	 *         from this input stream without blocking.
	 */
	public int available();

	/**
	 * Set the current marked position in the stream. ByteArrayInputStream
	 * objects are marked at position zero by default when constructed. They may
	 * be marked at another position within the buffer by this method.
	 * <p>
	 * If no mark has been set, then the value of the mark is the offset passed
	 * to the constructor (or 0 if the offset was not supplied).
	 * <p>
	 * Note: The <code>readAheadLimit</code> for this class has no meaning.
	 * 
	 * @param readAheadLimit It has no meaning.
	 */
	public void mark(final int readAheadLimit);

	/**
	 * Reads the next byte of data from this input stream. The value byte is
	 * returned as an <code>int</code> in the range <code>0</code> to
	 * <code>255</code>.
	 * 
	 * @exception EOFException If this buffer has reached the end.
	 * @return the next byte of data.
	 */
	public byte read() throws EOFException;

	/**
	 * Reads up to <code>len</code> bytes of data into an array of bytes from
	 * this input stream. The number <code>k</code> of bytes read is equal to
	 * the smaller of <code>len</code> and <code>count-pos</code>. If
	 * <code>k</code> is positive, then bytes <code>buf[pos]</code> through
	 * <code>buf[pos+k-1]</code> are copied into <code>b[off]</code> through
	 * <code>b[off+k-1]</code> in the manner performed by
	 * <code>System.arraycopy</code>. The value <code>k</code> is added into
	 * <code>pos</code> and <code>k</code> is returned.
	 * <p>
	 *
	 * @param b the buffer into which the data is read.
	 * @param off the start offset in the destination array <code>b</code>
	 * @param len the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer.
	 * @exception NullPointerException If <code>b</code> is <code>null</code>.
	 * @exception IndexOutOfBoundsException If <code>off</code> is negative,
	 *                <code>len</code> is negative, or <code>len</code> is
	 *                greater than <code>b.length - off</code>
	 * @exception EOFException If this buffer has reached the end.
	 */
	public int read(final byte[] b, final int off, int len) throws NullPointerException, IndexOutOfBoundsException, EOFException;

	/**
	 * Resets the buffer to the marked position. The marked position is 0 unless
	 * another position was marked or an offset was specified in the
	 * constructor.
	 */
	public void reset();

	/**
	 * Skips <code>n</code> bytes of input from this input stream. Fewer bytes
	 * might be skipped if the end of the input stream is reached. The actual
	 * number <code>k</code> of bytes to be skipped is equal to the smaller of
	 * <code>n</code> and <code>count-pos</code>. The value <code>k</code> is
	 * added into <code>pos</code> and <code>k</code> is returned.
	 *
	 * @param n the number of bytes to be skipped.
	 * @return the actual number of bytes skipped.
	 */
	public long skip(final long n);
}