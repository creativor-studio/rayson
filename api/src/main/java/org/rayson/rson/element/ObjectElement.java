/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.rson.element;

import java.util.Map;
import java.util.Set;

import org.rayson.api.annotation.NotNull;
import org.rayson.rson.RsonElement;
import org.rayson.rson.RsonType;

/**
 * {@link RsonElement} for {@link RsonType#OBJECT}.
 * 
 * @author creativor
 */
public interface ObjectElement extends RsonElement {

	/**
	 * Adds a member, which is a name-value pair, to self. The name must be a
	 * String, but the value can be an arbitrary RsonElement, thereby allowing
	 * you to build a full tree of RsonElements rooted at this node.
	 *
	 * @param property name of the member.
	 * @param value the member object.
	 */
	void add(final String property, @NotNull final RsonElement value);

	/**
	 * Returns a set of members of this object. The set is ordered.
	 *
	 * @return a unmodified set of members of this object.
	 */
	public Set<Map.Entry<String, RsonElement>> entrySet();

	/**
	 * Returns the member with the specified name.
	 *
	 * @param memberName name of the member that is being requested.
	 * @return the member matching the name. Null if no such member exists.
	 */
	public RsonElement get(final String memberName);

	/**
	 * Convenience method to check if a member with the specified name is
	 * present in this object.
	 *
	 * @param memberName name of the member that is being checked for presence.
	 * @return true if there is a member with the specified name, false
	 *         otherwise.
	 */
	public boolean has(final String memberName);

	/**
	 * @return Member count number of this object.
	 */
	public int memberCount();

}