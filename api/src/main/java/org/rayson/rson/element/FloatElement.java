/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#FLOAT}.
 * 
 * @author creativor
 */
public interface FloatElement extends PrimitiveElement {
	/**
	 * @return Float value of this element.
	 */
	public float getValue();
}