/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import org.rayson.api.annotation.NotNull;
import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#ARRAY}.
 * 
 * @author creativor
 */
public interface ArrayElement extends RsonElement, Iterable<RsonElement> {
	/**
	 * Add a item to this array.
	 * 
	 * @param itemEle Item element to be added.
	 */
	public void add(@NotNull final RsonElement itemEle);

	/**
	 * Returns the ith element of the array.
	 *
	 * @param i the index of the element that is being sought.
	 * @return the element present at the ith index.
	 * @throws IndexOutOfBoundsException if i is negative or greater than or
	 *             equal to the {@link #size()} of the array.
	 */
	public RsonElement get(final int i) throws IndexOutOfBoundsException;

	/**
	 * Returns the number of elements in the array.
	 *
	 * @return the number of elements in the array.
	 */
	public int size();
}