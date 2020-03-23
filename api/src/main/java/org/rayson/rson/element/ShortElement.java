/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#SHORT}.
 * 
 * @author creativor
 */
public interface ShortElement extends PrimitiveElement {

	/**
	 * @return Short value of this element.
	 */
	public short getValue();
}