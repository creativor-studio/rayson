/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#LONG}.
 * 
 * @author creativor
 */
public interface LongElement extends PrimitiveElement {
	/**
	 * @return Long value of this element.
	 */
	public long getValue();

}