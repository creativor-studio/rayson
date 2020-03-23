/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

/**
 * Parser used to parse {@link RsonElement} out of source byte array data.
 * 
 * @author creativor
 */
public interface RsonParser {

	/**
	 * Whether it has data remaining.
	 * 
	 * @return True if has data remaining not parsed yet.
	 */
	public boolean hasRemaining();

	/**
	 * Do parse.
	 * 
	 * @return Parsed {@link RsonElement}.
	 * @throws RsonException If failed to parse.
	 */
	public RsonElement parse() throws RsonException;

}