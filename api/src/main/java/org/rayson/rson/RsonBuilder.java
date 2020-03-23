/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

import org.rayson.rson.element.ArrayElement;
import org.rayson.rson.element.BigArrayElement;
import org.rayson.rson.element.BigObjectElement;
import org.rayson.rson.element.ByteArrayElement;
import org.rayson.rson.element.DoubleElement;
import org.rayson.rson.element.FloatElement;
import org.rayson.rson.element.IntElement;
import org.rayson.rson.element.LongElement;
import org.rayson.rson.element.ObjectElement;
import org.rayson.rson.element.ShortElement;
import org.rayson.rson.element.StringElement;

/**
 * An tool used to build RSON elements.
 * 
 * @author creativor
 *
 */
public interface RsonBuilder {
	/**
	 * Create a new RSON element of {@link RsonType#ARRAY}.
	 * 
	 * @return The created new element.
	 */
	public ArrayElement createArray();

	/**
	 * Create a new RSON element of {@link RsonType#BIG_ARRAY}.
	 * 
	 * @return The created new element.
	 */
	public BigArrayElement createBigArray();

	/**
	 * Create a new RSON element of {@link RsonType#BIG_OBJECT}.
	 * 
	 * @return The created new element.
	 */
	public BigObjectElement createBigObject();

	/**
	 * Create a new RSON element of {@link RsonType#BYTE_ARRAY}.
	 * 
	 * @return The created new element.
	 */
	public ByteArrayElement createByteArray();

	/**
	 * Create a new RSON element of {@link RsonType#DOUBLE}.
	 * 
	 * @param value Original value.
	 * @return The created new element.
	 */
	public DoubleElement createDoule(double value);

	/**
	 * Create a new RSON element of {@link RsonType#FLOAT}.
	 * 
	 * @param value Original value.
	 * @return The created new element.
	 */
	public FloatElement createFloat(float value);

	/**
	 * Create a new RSON element of {@link RsonType#INT}.
	 * 
	 * @param value Original value.
	 * @return The created new element.
	 */
	public IntElement createInt(int value);

	/**
	 * Create a new RSON element of {@link RsonType#LONG}.
	 * 
	 * @param value Original value.
	 * @return The created new element.
	 */
	public LongElement createLong(long value);

	/**
	 * Create a new RSON element of {@link RsonType#OBJECT}.
	 * 
	 * @return The created new element.
	 */
	public ObjectElement createObject();

	/**
	 * Create a new RSON element of {@link RsonType#SHORT}.
	 * 
	 * @param value Original value.
	 * @return The created new element.
	 */
	public ShortElement createShort(short value);

	/**
	 * Create a new RSON element of {@link RsonType#STRING}.
	 * 
	 * @param value Original value.
	 * @return The created new element.
	 */
	public StringElement createString(final String value);

}