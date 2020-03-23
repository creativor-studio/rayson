/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.demo.advanced.api;

import java.util.Objects;

import org.rayson.api.serial.RsonSerializable;

/**
 * Java bean can be serialized by Rayson framework.
 */
public class SerialObject implements RsonSerializable {

	private int intField;
	private String strField;

	/**
	 * @param intField the intField to set
	 */
	public void setIntField(int intField) {
		this.intField = intField;
	}

	/**
	 * @param strField the strField to set
	 */
	public void setStrField(String strField) {
		this.strField = strField;
	}

	/**
	 * @return the intField
	 */
	public int getIntField() {
		return intField;
	}

	/**
	 * @return the strField
	 */
	public String getStrField() {
		return strField;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { intField, strField };
		String[] keys = { "intField", "strField" };
		int keyLen = keys.length;
		sb.append("{");
		for (int i = 0; i < keyLen; i++) {
			sb.append("\"");
			sb.append(keys[i]);
			sb.append("\":\"");
			sb.append(values[i]);
			sb.append("\"");
			if (i != keyLen - 1)
				sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		// This is not symmetric!
		if (!(obj instanceof SerialObject))
			return false;
		final SerialObject that = (SerialObject) obj;
		if (!Objects.equals(this.intField, that.intField))
			return false;
		if (!Objects.equals(this.strField, that.strField))
			return false;
		return true;
	}
}
