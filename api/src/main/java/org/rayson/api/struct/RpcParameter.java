/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.struct;

import org.rayson.api.annotation.NotNull;

/**
 * RPC parameter.
 * 
 * @author Nick Zhang
 *
 */
public class RpcParameter {
	private final int index;
	private final String name;
	private final Class<?> type;
	private Object value;

	/**
	 * Construct new instance.
	 * 
	 * @param index Index on target java method parameters.
	 * @param name Parameter name.
	 * @param type Underling java class of this parameter.
	 * @throws IllegalArgumentException If got wrong parameters.
	 * 
	 */
	public RpcParameter(final int index, @NotNull String name, @NotNull Class<?> type) {

		if (name == null)
			throw new IllegalArgumentException("name should not be null");
		if (type == null)
			throw new IllegalArgumentException("type should not be null");
		this.index = index;
		this.name = name;
		this.type = type;
	}

	/**
	 * @return Index on target java method parameters.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return Name of this parameter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The underling java parameter's type associated with this
	 *         parameter.
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @return The actual value of this parameter.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Set up value of this parameter.
	 * 
	 * @param value Parameter value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { index, name, type, value };
		String[] keys = { "index", "name", "type", "value" };
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
