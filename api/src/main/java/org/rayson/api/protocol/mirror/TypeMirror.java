/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.mirror;

import org.rayson.api.Protocol;
import org.rayson.api.protocol.info.MetadataInfo;

/**
 * Represents a type in the Java programming language. It is used to handling
 * {@link Protocol} works on runtime.
 * 
 * @param <I> Meta data information type which associated with current type.
 * @param <S> Source object type.
 * @author Nick Zhang
 *
 */
interface TypeMirror<I extends MetadataInfo<?>, S> {
	/**
	 * @return Meta data information of this type mirror.
	 */
	public I getInfo();

	/**
	 * @return Source which this type mirror reflection to.
	 */
	public S getSource();
}