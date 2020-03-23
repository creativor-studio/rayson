/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.struct;

import java.util.Objects;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.ThreadSafe;

/**
 * Presents a data structure of (One Key , One Value). <br>
 * Values are in order.
 * 
 * @author creativor
 */
@ThreadSafe(false)
public class KeyValue {

	private String key;

	private String value;

	/**
	 * Construct a new key value pair.
	 * 
	 * @param key Key string.
	 * @param value Value string.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public KeyValue(@NotNull String key, String value) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		// This is not symmetric!
		if (!(obj instanceof KeyValue))
			return false;
		final KeyValue that = (KeyValue) obj;
		if (!Objects.equals(this.key, that.key))
			return false;
		return true;
	}

	/**
	 * @return Key of this key-value pair.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return Value of this key-value pair.
	 */
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.key);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { key, value };
		String[] keys = { "key", "value" };
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