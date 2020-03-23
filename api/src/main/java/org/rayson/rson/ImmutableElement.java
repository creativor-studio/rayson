/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson;

import java.util.Objects;

/**
 * Immutable {@link RsonElement}.
 */
final class ImmutableElement implements RsonElement {
	private final RsonType type;
	private final String typeStr;

	/**
	 * Construct a new immutable {@link RsonElement} of giving element type.
	 * 
	 * @param type RSON element's type.
	 */
	ImmutableElement(RsonType type) {
		this.type = type;
		this.typeStr = type.name().toLowerCase();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		// This is not symmetric!
		if (!(obj instanceof ImmutableElement))
			return false;
		final ImmutableElement that = (ImmutableElement) obj;
		if (!Objects.equals(this.type, that.type))
			return false;
		return true;
	}

	@Override
	public RsonType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public String toString() {
		return typeStr;
	}
}