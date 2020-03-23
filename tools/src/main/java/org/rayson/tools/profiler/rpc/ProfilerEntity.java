/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.profiler.rpc;

import java.util.Arrays;
import java.util.Objects;

import org.rayson.api.serial.RsonSerializable;

/**
 * An entity used for profiler testing.
 */
public class ProfilerEntity implements RsonSerializable {
	/**
	 * Builder to build {@link ProfilerEntity}.
	 */
	public static final class Builder {
		private ProfilerEntity wrapped;

		private Builder() {
			this.wrapped = new ProfilerEntity();
		}

		/**
		 * Builder method of the builder.
		 * 
		 * @return built class
		 */
		public ProfilerEntity build() {
			return wrapped;
		}

		/**
		 * Builder method for arrayField parameter.
		 * 
		 * @param arrayField build field arrayField .
		 * @return The builder itself.
		 */
		public Builder withArrayField(byte[] arrayField) {
			wrapped.arrayField = arrayField;
			return this;
		}

		/**
		 * Builder method for intField parameter.
		 * 
		 * @param intField build field intField .
		 * @return The builder itself.
		 */
		public Builder withIntField(int intField) {
			wrapped.intField = intField;
			return this;
		}

		/**
		 * Builder method for strField parameter.
		 * 
		 * @param strField build field strField .
		 * @return The builder itself.
		 */
		public Builder withStrField(String strField) {
			wrapped.strField = strField;
			return this;
		}
	}

	/**
	 * Creates builder to build {@link ProfilerEntity}.
	 * 
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	private byte[] arrayField;

	private int intField;

	private String strField;

	private ProfilerEntity() {

	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		Object[] values = { intField, strField, Arrays.toString(arrayField) };
		String[] keys = { "intField", "strField", "arrayField" };
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
		if (!(obj instanceof ProfilerEntity))
			return false;
		final ProfilerEntity that = (ProfilerEntity) obj;
		if (!Objects.equals(this.intField, that.intField))
			return false;
		if (!Objects.equals(this.strField, that.strField))
			return false;
		if (!Arrays.equals(this.arrayField, that.arrayField))
			return false;
		return true;
	}
}