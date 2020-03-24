/**
 * Copyright © 2019 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.test.integration.protocol;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.rayson.api.serial.RsonSerializable;

/**
 * An structure for testing RSON and JSON.
 */
public class TestStruct implements RsonSerializable {
	private final int i;
	private final boolean b;
	private final String s;
	private transient int t;
	private byte[] bytes;
	private String[] array;
	private List<String> list;
	private Map<String, String> map;

	public TestStruct(final int i, final boolean b, final String s, final int t) {
		this.i = i;
		this.b = b;
		this.s = s;
		this.t = t;
		this.list = List.of("item1", "item2");
		this.map = Map.of("k1", "v1", "k2", "v2");
		this.bytes = new byte[] { 2, 3, 5 };
		this.array = new String[] { "ab", "cd", "ef" };
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		// This is not symmetric!
		if (!(obj instanceof TestStruct))
			return false;
		final TestStruct that = (TestStruct) obj;
		if (!Objects.equals(this.i, that.i))
			return false;
		if (!Objects.equals(this.b, that.b))
			return false;
		if (!Objects.equals(this.s, that.s))
			return false;
		// Ignore field t.
		// if (!Objects.equals(this.t, that.t))
		// return false;
		if (!Arrays.equals(this.bytes, that.bytes))
			return false;
		if (!Arrays.equals(this.array, that.array))
			return false;
		if (!Objects.equals(this.list, that.list))
			return false;
		if (!Objects.equals(this.map, that.map))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Object[] values = { b, i, s, t };
		final String[] keys = { "b", "i", "s", "t" };
		final int keyLen = keys.length;
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