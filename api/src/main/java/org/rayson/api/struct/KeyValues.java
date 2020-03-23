/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.struct;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.ThreadSafe;
import org.rayson.api.serial.JsonStringBuilder;

/**
 * Presents a data structure of
 * 
 * <pre>
 * &lt;One Key : Multiple Values&gt;
 * </pre>
 * 
 * Values are in order.
 * 
 */
@ThreadSafe(false)
public class KeyValues {

	private String key;

	private Set<String> values;

	/**
	 * Construct a new key values, with specified key.
	 * 
	 * @param key Specified key.
	 * @throws IllegalArgumentException If the key is <code>null</code>.
	 */
	public KeyValues(@NotNull String key) {
		if (key == null)
			throw new IllegalArgumentException("key should not be null");
		this.key = key;
		this.values = new HashSet<>();
	}

	/**
	 * Add a new value to this container.
	 * 
	 * @param value New value.
	 * @throws IllegalArgumentException If the value is <code>null</code>.
	 */
	public void addValue(@NotNull String value) {
		if (value == null)
			throw new IllegalArgumentException("value should not be null");

		this.values.add(value);
	}

	/**
	 * @return The key of this container.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the values collection. Unmodifiable.
	 */
	public Collection<String> getValues() {
		return Collections.unmodifiableCollection(values);
	}

	@Override
	public String toString() {
		JsonStringBuilder b = new JsonStringBuilder(this);
		b.append("key", key).append("values", values);
		return b.toJson();
	}
}