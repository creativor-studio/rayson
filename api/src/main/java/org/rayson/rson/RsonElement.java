/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

import java.nio.charset.Charset;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.serial.BufferWritable;
import org.rayson.api.serial.ByteArrayInputBuffer;
import org.rayson.api.serial.ByteArrayOutputBuffer;

/**
 * Stands for an element of RSON.
 * 
 * @see RsonType About supported element types.
 * @author creativor
 */
public interface RsonElement extends BufferWritable {

	/**
	 * An immutable {@link RsonElement} for {@link RsonType#NULL}.
	 */
	public static final RsonElement NULL = new ImmutableElement(RsonType.NULL);

	/**
	 * An immutable {@link RsonElement} for {@link RsonType#TRUE}.
	 */
	public static final RsonElement TRUE = new ImmutableElement(RsonType.TRUE);

	/**
	 * An immutable {@link RsonElement} for {@link RsonType#FALSE}.
	 */
	public static final RsonElement FALSE = new ImmutableElement(RsonType.FALSE);

	/**
	 * Compares the specified object with this element for equality.
	 *
	 * @param obj the object to be compared for equality with this element.
	 * @return {@code true} if the specified object is equal to this element.
	 */
	@Override
	public boolean equals(Object obj);

	/**
	 * @return The Rson type of this element.
	 */
	@NotNull
	public RsonType getType();

	/**
	 * Returns the hash code value for this element.
	 *
	 * @return the hash code value for this element.
	 */
	@Override
	public int hashCode();

	/**
	 * Read content of this element from <code>buffer</code>.By default, it do
	 * nothing.
	 * 
	 * @param buffer Input buffer.
	 * @param charset The character set which the result {@link RsonElement}
	 *            used.
	 * @throws RsonException If json input is wrong format.
	 */
	default void readContent(final ByteArrayInputBuffer buffer, Charset charset) throws RsonException {
		// Do nothing.
	}

	/**
	 * Write content of this element to <code>buffer</code>.By default, it do
	 * nothing.
	 * 
	 * @param buffer Output buffer.
	 * @param charset The character set of the content.
	 * 
	 */
	default void writeContent(final ByteArrayOutputBuffer buffer, Charset charset) {
		// Do nothing.
	}

	@Override
	public default void writeTo(final ByteArrayOutputBuffer buffer, Charset charset) {
		buffer.write(getType().getType());
		writeContent(buffer, charset);
	}
}