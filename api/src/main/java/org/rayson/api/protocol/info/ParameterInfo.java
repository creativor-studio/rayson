/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.info;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 * Description an method parameter of {@link MethodInfo}.
 * 
 * @author Nick Zhang
 *
 */
public class ParameterInfo implements MetadataInfo<JsonObject> {
	private int index;
	private String name;
	private String comment;

	ParameterInfo() {

	}

	/**
	 * Construct a new instance.
	 * 
	 * @param index Index on the target method's parameter list.
	 * @param name Name of this parameter.
	 * @param comment Comment document of this parameter.
	 */
	public ParameterInfo(int index, String name, String comment) {
		super();
		this.index = index;
		this.name = name;
		this.comment = comment;
	}

	@Override
	public void fromJson(JsonObject json) {

		this.name = getString(json, "name");

		this.comment = getString(json, "comment");

		Integer i = json.getInt("index");
		if (i != null) {
			this.index = i;
		}

	}

	/**
	 * @return Comment document of this parameter.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return The index of this parameter in enclosing method. Start from 0.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return Name of this parameter.In run time, this name should refer to the
	 *         the java reflection.
	 */
	public String getName() {
		return name;
	}

	@Override
	public void toJson(JsonGenerator generator) {

		generator.writeStartObject();
		generator.write("name", name);
		generator.write("index", index);
		if (comment != null)
			generator.write("comment", comment);
		else
			generator.writeNull("comment");

		generator.writeEnd();// writeStartObject
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { name, comment };
		String[] keys = { "name", "comment" };
		int keyLen = keys.length;
		sb.append("{");
		for (int i = 0; i < keyLen; i++) {
			sb.append(keys[i]);
			sb.append(":");
			sb.append(values[i]);
			if (i != keyLen - 1)
				sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
}