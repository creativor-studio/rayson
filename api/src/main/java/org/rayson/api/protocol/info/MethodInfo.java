/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

import org.rayson.api.Protocol;

/**
 * Description of a method API which defined in {@link Protocol}.
 * 
 * @author Nick Zhang
 */
public final class MethodInfo implements MetadataInfo<JsonObject> {
	private String name;
	private String comment;
	private String returnType;
	private String returnComment;
	private List<ParameterInfo> parameters;
	private List<AnnotationInfo> annotations;

	MethodInfo() {
		parameters = new ArrayList<ParameterInfo>();
		annotations = new ArrayList<>();
	}

	/**
	 * Construct a new method information with full meta data.
	 * 
	 * @param name Method name.
	 * @param comment Method comments.
	 * @param returnType Method java return type.
	 * @param returnComment Return value comments.
	 * @param parameters Parameter informations.
	 * @param annotations Annotation informations on target java method.
	 */
	public MethodInfo(String name, String comment, String returnType, String returnComment, List<ParameterInfo> parameters, List<AnnotationInfo> annotations) {
		this();
		this.name = name;
		this.comment = comment;
		this.returnType = returnType;
		this.returnComment = returnComment;
		this.parameters.addAll(parameters);
		this.annotations.addAll(annotations);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof MethodInfo))
			return false;
		final MethodInfo that = (MethodInfo) obj;
		if (!Objects.equals(this.name, that.name))
			return false;
		return true;
	}

	@Override
	public void fromJson(JsonObject json) {

		this.name = getString(json, "name");
		this.comment = getString(json, "comment");
		this.returnType = getString(json, "returnType");
		this.returnComment = getString(json, "returnComment");

		JsonArray ja = json.getJsonArray("annotations");
		AnnotationInfo annoInfo;
		if (ja != null) {
			for (JsonValue jv : ja) {
				annoInfo = new AnnotationInfo();
				annoInfo.fromJson((JsonString) jv);
				this.annotations.add(annoInfo);
			}
		}
		ja = json.getJsonArray("parameters");
		ParameterInfo pInfo;
		if (ja != null) {
			for (JsonValue jv : ja) {
				pInfo = new ParameterInfo();
				pInfo.fromJson(jv.asJsonObject());
				this.parameters.add(pInfo);
			}
		}
	}

	/**
	 * @return the annotations.Cloned.
	 */
	public AnnotationInfo[] getAnnotations() {
		return annotations.toArray(new AnnotationInfo[0]);
	}

	/**
	 * @return Return the text of the comment for this method. Tags have been
	 *         removed.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * @return The associated method name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Find parameter information by giving <code>parameterIndex</code>.
	 * 
	 * @param parameterIndex Parameter index.
	 * @return Parameter info matched the parameter name. Or else,
	 *         <code>null</code>.
	 */
	public ParameterInfo getParameter(int parameterIndex) {
		if (parameterIndex < 0)
			return null;
		for (ParameterInfo parameterInfo : parameters) {
			if (parameterInfo.getIndex() == parameterIndex)
				return parameterInfo;
		}
		return null;
	}

	/**
	 * Find parameter information by giving <code>parameterName</code>.
	 * 
	 * @param parameterName Parameter name.
	 * @return Parameter info matched the parameter name. Or else,
	 *         <code>null</code>.
	 */
	public ParameterInfo getParameter(String parameterName) {
		if (parameterName == null || parameterName.isEmpty())
			return null;
		for (ParameterInfo parameterInfo : parameters) {
			if (parameterName.equals(parameterInfo.getName()))
				return parameterInfo;
		}
		return null;
	}

	/**
	 * @return the parameters.Cloned.
	 */
	public ParameterInfo[] getParameters() {
		return parameters.toArray(new ParameterInfo[0]);
	}

	/**
	 * @return The java comments on return value.
	 */
	public String getReturnComment() {
		return returnComment;
	}

	/**
	 * @return The type of return value.
	 */
	public String getReturnType() {
		return returnType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	@Override
	public void toJson(JsonGenerator generator) {
		generator.writeStartObject();
		generator.write("name", name);
		if (comment != null)
			generator.write("comment", comment);
		else
			generator.writeNull("comment");

		generator.write("returnType", returnType);

		if (returnComment != null)
			generator.write("returnComment", returnComment);
		else
			generator.writeNull("returnComment");

		generator.writeStartArray("annotations");
		for (AnnotationInfo annotation : annotations) {
			annotation.toJson(generator);
		}
		generator.writeEnd();// writeStartArray

		generator.writeStartArray("parameters");
		for (ParameterInfo parameter : parameters) {
			parameter.toJson(generator);
		}
		generator.writeEnd();// writeStartArray

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