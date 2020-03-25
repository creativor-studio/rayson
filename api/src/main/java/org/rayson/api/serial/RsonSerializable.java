/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.serial;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.SerializedName;
import org.rayson.rson.RsonElement;
import org.rayson.rson.element.ObjectElement;

/**
 * An interface which means that the object can be serialized to
 * {@link RsonElement} and de-serialized from RSON element.<br>
 * By default, we using built-in strategy to serialize and de-serialize the
 * {@link RsonSerializable} objects. <br>
 * <p>
 * But we can also customize the strategy in the following ways:<br>
 * <br>
 * 
 * - Specified the serialize strategy: Implements interface
 * {@link WriteStrategy}.<br>
 *
 * - Specified the de-serialize strategy:Implements interface
 * {@link ReadStrategy}.
 * 
 * 
 * @see SerializedName About how to re-define the field name when serializing.
 */
public interface RsonSerializable {
	/**
	 * An strategy to read an object out of giving Json value.
	 *
	 */
	public static interface ReadStrategy {
		/**
		 * Build this object from giving Json value.
		 * 
		 * @param value Json format value to converted to java object.
		 */
		public void fromRson(@NotNull ObjectElement value);
	}

	/**
	 * An strategy to write an object to Json value.
	 */
	public static interface WriteStrategy {
		/**
		 * Convert this object to Json value.
		 * 
		 * @return Target json value.
		 */
		public @NotNull ObjectElement toRson();
	}
}