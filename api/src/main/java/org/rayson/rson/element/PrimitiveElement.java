/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;

/**
 * A class representing a RSON primitive value. A primitive value is either a
 * String, a Java primitive, or a Java primitive wrapper type.
 */
public interface PrimitiveElement extends RsonElement {

	/**
	 * Check whether this primitive contains a boolean value.
	 *
	 * @return true if this primitive contains a boolean value, false otherwise.
	 */
	boolean isBoolean();

	/**
	 * convenience method to get this element as a boolean value.
	 *
	 * @return get this element as a primitive boolean value.
	 */
	boolean getAsBoolean();

	/**
	 * Check whether this primitive contains a Number.
	 *
	 * @return true if this primitive contains a Number, false otherwise.
	 */
	boolean isNumber();

	/**
	 * convenience method to get this element as a Number.
	 *
	 * @return get this element as a Number.
	 * @throws NumberFormatException if the value contained is not a valid
	 *             Number.
	 */
	Number getAsNumber();

	/**
	 * Check whether this primitive contains a String value.
	 *
	 * @return true if this primitive contains a String value, false otherwise.
	 */
	boolean isString();

	/**
	 * convenience method to get this element as a String.
	 *
	 * @return get this element as a String.
	 */
	String getAsString();

	/**
	 * convenience method to get this element as a primitive double.
	 *
	 * @return get this element as a primitive double.
	 * @throws NumberFormatException if the value contained is not a valid
	 *             double.
	 */
	double getAsDouble();

	/**
	 * convenience method to get this element as a float.
	 *
	 * @return get this element as a float.
	 * @throws NumberFormatException if the value contained is not a valid
	 *             float.
	 */
	float getAsFloat();

	/**
	 * convenience method to get this element as a primitive long.
	 *
	 * @return get this element as a primitive long.
	 * @throws NumberFormatException if the value contained is not a valid long.
	 */
	long getAsLong();

	/**
	 * convenience method to get this element as a primitive short.
	 *
	 * @return get this element as a primitive short.
	 * @throws NumberFormatException if the value contained is not a valid short
	 *             value.
	 */
	short getAsShort();

	/**
	 * convenience method to get this element as a primitive integer.
	 *
	 * @return get this element as a primitive integer.
	 * @throws NumberFormatException if the value contained is not a valid
	 *             integer.
	 */
	int getAsInt();

	/**
	 * convenience method to get this element as a primitive byte.
	 *
	 * @return get this element as a primitive byte.
	 * @throws NumberFormatException if the value contained is not a valid byte.
	 */
	byte getAsByte();

	/**
	 * convenience method to get this element as a primitive character.
	 *
	 * @return get this element as a primitive character.
	 */
	char getAsCharacter();

}