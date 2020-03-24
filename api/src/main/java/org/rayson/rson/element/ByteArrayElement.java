/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import java.util.Base64;

import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#ARRAY}.
 * 
 * @author creativor
 */
public interface ByteArrayElement extends RsonElement {
	/**
	 * @return Byte array data of this element.
	 */
	public byte[] getValue();

	/**
	 * @return {@link Base64} encoding string ,which represent the byte array
	 *         value of this element.
	 */
	public String getBase64();

}