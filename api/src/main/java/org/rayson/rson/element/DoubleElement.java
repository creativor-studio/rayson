/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#DOUBLE}.
 * 
 * @author creativor
 */
public interface DoubleElement extends PrimitiveElement {
	/**
	 * @return Double value of this element.
	 */
	public double getValue();

}