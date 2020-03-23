/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HTTP method enums.
 * 
 * @author creativor
 */
public enum HttpMethod {
	/**
	 * DELETE HTTP method.
	 */
	DELETE,
	/**
	 * GET HTTP method.
	 */ 
	GET,
	/**
	 * OPTIONS HTTP method.
	 */
	OPTIONS,
	/**
	 * POST HTTP method.
	 */
	POST,
	/**
	 * PUT HTTP method.
	 */
	PUT,;

	private static final class FirstByteMethods {
		private final byte firstByte;

		private final Set<SecondByteMethod> secondBytes;

		FirstByteMethods(final byte b) {
			this.firstByte = b;
			secondBytes = new HashSet<>();
		}

		void addMethod(final HttpMethod method) {
			secondBytes.add(new SecondByteMethod(method));
		}

		@Override
		public int hashCode() {
			return firstByte;
		}

		HttpMethod matchMethod(final ByteBuffer bytes, final int position, final int count) {
			int secondByte = bytes.get(position + 1);
			HttpMethod method = null;
			for (SecondByteMethod sbm : secondBytes) {
				if (sbm.method.bytes[1] == secondByte) {
					method = sbm.method;
					break;
				}
			}

			if (method == null)
				return null;

			if (count == 2)
				return method;

			int methodLeftCount = method.bytes.length - 2;

			int dataLeftCount = count - 2;

			int leftCount = Math.min(dataLeftCount, methodLeftCount);

			for (int i = 0; i < leftCount; i++) {
				if (bytes.get(position + 2 + i) != method.bytes[2 + i])
					return null;
			}

			return method;
		}
	}

	private static final class SecondByteMethod {
		private final HttpMethod method;

		SecondByteMethod(final HttpMethod method) {
			this.method = method;
		}

		@Override
		public int hashCode() {
			return this.method.bytes[1];
		}

	}

	private static final Map<Byte, FirstByteMethods> FIRST_BYTES;

	private static final HttpMethod DEFAULT_METHOD = HttpMethod.GET;

	static {
		FIRST_BYTES = new HashMap<>();
		for (HttpMethod v : values()) {
			byte firstByte = v.bytes[0];
			FirstByteMethods fbm = FIRST_BYTES.get(firstByte);
			if (fbm == null) {
				fbm = new FirstByteMethods(firstByte);
				FIRST_BYTES.put(firstByte, fbm);
			}

			fbm.addMethod(v);
		}
	}

	/**
	 * Lookup the giving byte array data and find an matched {@link HttpMethod}.
	 * 
	 * @param bytes Array containing ISO-8859-1 characters.
	 * @param position The first valid index.
	 * @param limit The first non valid index.
	 * @return Matched {@link HttpMethod}. Or {@link #GET} for default.
	 * @throws IllegalArgumentException If got wrong parameters.
	 */
	public static HttpMethod lookAheadGet(final ByteBuffer bytes, final int position, final int limit) {
		if (position < 0)
			throw new IllegalArgumentException("position should not be nagtive");

		int count = limit - position;

		if (count < 2)
			throw new IllegalArgumentException("testing count should larger then 2");
		int firstByte = bytes.get(position);

		FirstByteMethods fbm = null;

		for (Entry<Byte, FirstByteMethods> entry : FIRST_BYTES.entrySet()) {
			if (firstByte == entry.getKey()) {
				fbm = entry.getValue();
				break;
			}
		}

		if (fbm == null)
			return DEFAULT_METHOD;

		HttpMethod method = fbm.matchMethod(bytes, position, count);
		return (method == null) ? DEFAULT_METHOD : method;
	}

	/**
	 * Test whether the giving byte array data match one of {@link HttpMethod}.
	 * 
	 * @param bytes Array containing ISO-8859-1 characters.
	 * @param position The first valid index.
	 * @param limit The first non valid index.
	 * @return True if the source data match one of {@link HttpMethod}. Or
	 *         <code>false</code> if the source data do not match any
	 *         {@link HttpMethod}.
	 * @throws IllegalArgumentException If got wrong parameters .
	 * 
	 */
	public static boolean matchMethod(final ByteBuffer bytes, final int position, final int limit) {
		if (position < 0)
			throw new IllegalArgumentException("position should not be nagtive");

		int count = limit - position;

		if (count < 1)
			throw new IllegalArgumentException("testing count should larger then 0");

		int firstByte = bytes.get(position);

		FirstByteMethods fbm = null;

		for (Entry<Byte, FirstByteMethods> entry : FIRST_BYTES.entrySet()) {
			if (firstByte == entry.getKey()) {
				fbm = entry.getValue();
				break;
			}
		}

		if (fbm == null)
			return false;
		else if (count == 1) {
			return true;
		}

		return fbm.matchMethod(bytes, position, count) != null;
	}

	/**
	 * Find {@link HttpMethod} which match the giving <code>str</code>.
	 * 
	 * @param str Method string.
	 * @return Matched item. Or not found, {@link #GET} return.
	 */
	public static HttpMethod methodOf(final String str) {
		if (str == null || str.isEmpty())
			return GET;

		for (HttpMethod v : values()) {
			if (v.name().equals(str))
				return v;
		}

		return GET;
	}

	private final byte[] bytes;

	private HttpMethod() {
		bytes = this.name().getBytes(HttpHeader.CHARSET);
	}

	/**
	 * @return Byte array presentation of this method.
	 */
	public byte[] toBytes() {
		return bytes;
	}
}