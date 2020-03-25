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
	 * An element stands for an <code>null</code> object.
	 */
	NULL(0),
	/**
	 * An element stands for an boolean value <code>true</code>.
	 */
	TRUE(1),
	/**
	 * An element stands for an boolean value <code>false</code>.
	 */
	FALSE(2),
	/**
	 * For an {@link Byte} element.
	 */
	BYTE(3),
	/**
	 * For an {@link Short} element.
	 */
	SHORT(4),
	/**
	 * For an {@link Integer} element.
	 */
	INT(5),
	/**
	 * For an {@link Float} element.
	 */
	FLOAT(6),
	/**
	 * For an {@link Long} element.
	 */
	LONG(7),
	/**
	 * For an {@link Double} element.
	 */
	DOUBLE(8),
	/**
	 * For an {@link String} element.
	 */
	STRING(16),
	/**
	 * For an array element.
	 */
	ARRAY(32),
	/**
	 * For an big array element, which means the items count larger than
	 * {@value Byte#MAX_VALUE}.
	 */
	BIG_ARRAY(33),
	/**
	 * For an object element.
	 */
	OBJECT(64),
	/**
	 * For an big object element, which means the members count larger than
	 * {@value Byte#MAX_VALUE}.
	 */
	BIG_OBJECT(65),
	/**
	 * For an byte array element.
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