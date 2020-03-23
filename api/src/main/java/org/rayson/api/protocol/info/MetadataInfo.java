/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.info;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonGenerator;

import org.rayson.api.Protocol;
import org.rayson.api.serial.RsonSerializable;

/**
 * Information for Rayson {@link Protocol} meta data.
 * 
 * @param <J> Json value type.
 * @author Nick Zhang
 */
public interface MetadataInfo<J extends JsonValue> extends RsonSerializable {
	/**
	 * Build this object from given {@link JsonObject}.
	 * 
	 * @param json Source json value.
	 */
	public void fromJson(J json);

	/**
	 * Get sting value of the json object's property.
	 * 
	 * @param json JSON object.
	 * @param name Property name. <code>null</code>.
	 * @return String value of the target property. Or <code>null</code> if not
	 *         found.
	 * @throws IllegalArgumentException If one of the arguments is
	 */
	default String getString(JsonObject json, String name) {
		if (json == null)
			throw new IllegalArgumentException("json should not be null");
		if (name == null)
			throw new IllegalArgumentException("name should not be null");

		JsonValue c = json.get(name);
		if (c == null)
			return null;
		if (c.getValueType() == ValueType.STRING) {
			return ((JsonString) c).getString();
		}
		return null;
	}

	/**
	 * Convert this bean to JSON.
	 * 
	 * @param generator Json generator which help to generate JSON.
	 */
	public void toJson(JsonGenerator generator);
}
