/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.protocol.mirror;

import java.lang.reflect.Parameter;

import org.rayson.api.protocol.info.ParameterInfo;

/**
 * Mirror of one method parameter of {@link MethodMirror}.
 * 
 * @author Nick Zhang
 *
 */
public class ParameterMirror implements TypeMirror<ParameterInfo, Parameter> {
	/**
	 * 
	 */
	public static final String VARIABLE_NAME_PREFIX = "p";

	private int index;
	private ParameterInfo info;
	private String name;

	private Parameter source;

	/**
	 * @param name Name of the new parameter mirror.
	 * @param index Index of the new parameter mirror in parent method.
	 * @param source Source java parameter of the new parameter mirror.
	 * @param info Information of the new parameter mirror.
	 */
	public ParameterMirror(String name, int index, Parameter source, ParameterInfo info) {
		super();
		this.name = name;
		this.index = index;
		this.source = source;
		this.info = info;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	@Override
	public ParameterInfo getInfo() {
		return info;
	}

	/**
	 * @return The name of this parameter.
	 */
	public String getName() {
		return name;
	}

	@Override
	public Parameter getSource() {
		return source;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { name, index };
		String[] keys = { "name", "index" };
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