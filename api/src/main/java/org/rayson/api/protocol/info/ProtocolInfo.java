/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

import org.rayson.api.Protocol;

/**
 * Information of one {@link Protocol}.
 * 
 * @author Nick Zhang
 *
 */
public class ProtocolInfo implements MetadataInfo<JsonObject> {
	private static final String VERSION = "1.0";
	private String version = VERSION;
	private String name;
	private String comment;
	private MethodInfo[] methods;

	/**
	 * Construct an blank object.
	 */
	public ProtocolInfo() {

	}

	/**
	 * Construct a new instance.
	 * 
	 * @param name Protocol name, it equals to the target protocol interface's
	 *            full name.
	 * @param comment The protocol's comment document.
	 * @param methods Method info map.
	 */
	public ProtocolInfo(String name, String comment, Set<MethodInfo> methods) {
		super();
		this.name = name;
		this.comment = comment;
		this.methods = methods.toArray(new MethodInfo[0]);
	}

	@Override
	public void fromJson(JsonObject json) {

		this.version = getString(json, "version");
		this.name = getString(json, "name");
		this.comment = getString(json, "comment");

		MethodInfo mInfo;
		JsonArray ja = json.getJsonArray("methods");
		List<MethodInfo> list = new ArrayList<>();
		if (ja != null) {
			for (JsonValue jv : ja) {
				mInfo = new MethodInfo();
				mInfo.fromJson(jv.asJsonObject());
				list.add(mInfo);
			}
		}
		this.methods = list.toArray(new MethodInfo[0]);
	}

	/**
	 * @return The text of the comment for this method. Tags have been removed.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * Find method information by giving <code>methodName</code>
	 * 
	 * @param methodName Method name.
	 * @return Method info matched the method name. Or else, <code>null</code>.
	 */
	public MethodInfo getMethod(String methodName) {
		if (methodName == null || methodName.isEmpty())
			return null;
		for (MethodInfo m : methods) {
			if (m.getName().equals(methodName))
				return m;
		}
		return null;
	}

	/**
	 * @return the methods list.Cloned.
	 */
	public MethodInfo[] getMethods() {
		return methods.clone();
	}

	/**
	 * @return The associated method name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Version of the target protocol definition.
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public void toJson(JsonGenerator generator) {
		generator.writeStartObject();
		generator.write("version", version);
		generator.write("name", name);
		if (comment != null)
			generator.write("comment", comment);
		else
			generator.writeNull("comment");
		generator.writeStartArray("methods");
		for (MethodInfo methodInfo : methods) {
			methodInfo.toJson(generator);
		}
		generator.writeEnd();// writeStartArray

		generator.writeEnd();// writeStartObject
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"name\":");
		sb.append("\"" + name + "\":");
		sb.append(",");
		sb.append("\"comment\":");
		sb.append("\"" + comment + "\":");
		sb.append(",");
		sb.append("\"methods\":");
		sb.append(Arrays.toString(methods));
		sb.append("}");
		return sb.toString();
	}
}