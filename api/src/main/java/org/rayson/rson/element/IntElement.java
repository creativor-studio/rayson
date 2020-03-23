/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#INT}.
 * 
 * @author creativor
 */
public interface IntElement extends PrimitiveElement {
	/**
	 * @return Integer value of this element.
	 */
	public int getValue();
}