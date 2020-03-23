/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

/**
 * Type enum for {@link RsonElement}.
 * 
 * @author creativor
 */
public enum RsonType {
	/**
	 * For null element.
	 */
	NULL(0),
	/**
	 * For <code>true</code> element.
	 */
	TRUE(1),
	/**
	 * For <code>false</code> element.
	 */
	FALSE(2),
	/**
	 * For byte element.
	 */
	BYTE(3),
	/**
	 * For short number element.
	 */
	SHORT(4),
	/**
	 * For integer number element.
	 */
	INT(5),
	/**
	 * For float number element.
	 */
	FLOAT(6),
	/**
	 * For long number element.
	 */
	LONG(7),
	/**
	 * For double number element.
	 */
	DOUBLE(8),
	/**
	 * For string element.
	 */
	STRING(16),
	/**
	 * For array element.
	 */
	ARRAY(32),
	/**
	 * For an big array element, which means the items count larger than
	 * {@value Byte#MAX_VALUE}.
	 */
	BIG_ARRAY(33),
	/**
	 * For object element.
	 */
	OBJECT(64),
	/**
	 * For an big object element, which means the members count larger than
	 * {@value Byte#MAX_VALUE}.
	 */
	BIG_OBJECT(65),
	/**
	 * For an byte array RSON element.
	 */
	BYTE_ARRAY(127);

	/**
	 * Find a type of giving a <code>type</code> number.
	 * 
	 * @param type Type number.
	 * @return Matched type.
	 * @throws RsonException If not found matched type.
	 */
	public static final RsonType typeOf(final int type) throws RsonException {
		if (type < 0)
			throw new RsonException("No such type found");

		for (final RsonType t : values()) {
			if (t.type == type)
				return t;
		}

		throw new RsonException("No such type found");
	}

	private byte type;

	private RsonType(final int type) {
		this.type = (byte) type;
	}

	/**
	 * The type number in byte.
	 * 
	 * @return Number of this type.
	 */
	public byte getType() {
		return type;
	}
}