/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.serial;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An tool used to convert an java object to JSON string, when generating
 * toString() method java codes automatically by IDE .
 * 
 * @author Nick Zhang
 *
 */
public final class JsonStringBuilder {
	private static final String NULL_OBJECT_TOKEN = "null";

	private static void buildArrayJsonValue(Object array, StringBuilder b) {
		int iMax = Array.getLength(array) - 1;
		if (iMax == -1) {
			b.append("[]");
			return;
		}

		b.append('[');
		Object item;
		for (int i = 0;; i++) {
			item = Array.get(array, i);
			buildObjectJsonValue(item, b);
			if (i == iMax) {
				b.append(']').toString();
				return;
			}
			b.append(", ");
		}
	}

	private static void buildArrayJsonValue(Object[] array, StringBuilder b) {
		if (array == null) {
			b.append(NULL_OBJECT_TOKEN);
			return;
		}

		int iMax = array.length - 1;
		if (iMax == -1) {
			b.append("[]");
			return;
		}

		b.append('[');
		Object item;
		for (int i = 0;; i++) {
			item = array[i];
			buildObjectJsonValue(item, b);
			if (i == iMax) {
				b.append(']').toString();
				return;
			}
			b.append(", ");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void buildMapJsonValue(Map map, StringBuilder b) {

		if (map == null) {
			b.append(NULL_OBJECT_TOKEN);
			return;
		}

		Set<Entry<?, ?>> entrySet = map.entrySet();
		JsonStringBuilder mapBuilder = new JsonStringBuilder(map);

		for (Entry<?, ?> entry : entrySet) {
			mapBuilder.append(entry.getKey().toString(), entry.getValue());
		}
		b.append(mapBuilder.toJson());
	}

	@SuppressWarnings("rawtypes")
	private static void buildObjectJsonValue(Object value, StringBuilder b) {

		if (value == null) {
			b.append(NULL_OBJECT_TOKEN);
			return;
		}

		if (value instanceof String) {
			b.append("\"");
			b.append(value);
			b.append("\"");
			return;
		}

		if (value instanceof Set) {
			buildArrayJsonValue(((Set) value).toArray(), b);
			return;
		}
		if (value instanceof List) {
			buildArrayJsonValue(((List) value).toArray(), b);
			return;
		}
		if (value instanceof Map) {
			buildMapJsonValue((Map) value, b);
			return;
		}
		Class<?> type = value.getClass();

		if (type.isPrimitive()) {
			b.append(value);
			return;
		}

		if (type.isArray()) {
			buildArrayJsonValue(value, b);
			return;
		}

		b.append(value.toString());
	}

	private StringBuilder buffer;

	private int fieldCount;

	private Object obj;

	/**
	 * Construct a new build to convert given <code>obj</code> to json string.
	 * 
	 * @param obj Target object.
	 */
	public JsonStringBuilder(Object obj) {
		this.obj = obj;
		buffer = new StringBuilder();
		buffer.append("{");
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>boolean</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, boolean value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>boolean</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>hashCode</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, boolean[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>byte</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, byte value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>byte</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, byte[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>char</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, char value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>char</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, char[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>double</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, double value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>double</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, double[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>float</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, float value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>float</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, float[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>int</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, int value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>int</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, int[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>long</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, long value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>long</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, long[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>Object</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param object the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, Object object) {
		appendObject(fieldName, object);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>Object</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, Object[] array) {
		appendArray(fieldName, array);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>short</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param value the value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, short value) {
		appendPrimitive(fieldName, value);
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> a <code>short</code> array.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param array the array to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, short[] array) {
		appendPrimitiveArray(fieldName, Arrays.toString(array));
		return this;
	}

	/**
	 * <p>
	 * Append to the <code>toString</code> an <code>String</code> value.
	 * </p>
	 *
	 * @param fieldName the field name
	 * @param str the string value to add to the <code>toString</code>
	 * @return this
	 */
	public JsonStringBuilder append(String fieldName, String str) {
		appendObject(fieldName, str);
		return this;
	}

	private void appendArray(String fieldName, Object[] array) {
		if (fieldCount++ > 0)
			buffer.append(", ");
		buffer.append("\"").append(fieldName).append("\": ");

		buildArrayJsonValue(array, buffer);
	}

	private void appendObject(String fieldName, Object value) {
		if (fieldCount++ > 0)
			buffer.append(", ");
		buffer.append("\"").append(fieldName).append("\": ");
		buildObjectJsonValue(value, buffer);
	}

	private void appendPrimitive(String fieldName, Object value) {
		if (fieldCount++ > 0)
			buffer.append(", ");
		buffer.append("\"").append(fieldName).append("\": ").append(value);
	}

	private void appendPrimitiveArray(String fieldName, String arrayStr) {
		if (fieldCount++ > 0)
			buffer.append(", ");
		buffer.append("\"").append(fieldName).append("\": ").append(arrayStr);
	}

	/**
	 * @return The source object used to build json sting.
	 */
	protected Object getObject() {
		return obj;
	}

	/**
	 * @return the Json format description of the target object.
	 */
	public String toJson() {
		buffer.append("}");
		return buffer.toString();
	}
}