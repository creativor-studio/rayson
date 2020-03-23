/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#BYTE}.
 * 
 * @author creativor
 */
public interface ByteElement extends PrimitiveElement {
	/**
	 * @return Byte value of this element.
	 */
	public byte getValue();
}