/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#STRING}.
 * 
 * @author creativor
 */
public interface StringElement extends PrimitiveElement {
	/**
	 * @return String value of this element.
	 */
	public String getValue();
}