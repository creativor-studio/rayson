/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP version enum.
 */
public enum HttpVersion {
	/**
	 * HTTP version 0.9.
	 */
	HTTP_0_9("HTTP/0.9", 9),
	/**
	 * HTTP version 1.0.
	 */
	HTTP_1_0("HTTP/1.0", 10),
	/**
	 * HTTP version 1.1.
	 */
	HTTP_1_1("HTTP/1.1", 11),
	/**
	 * HTTP version 2.0.
	 */
	HTTP_2_0("HTTP/2.0", 20);

	/* ------------------------------------------------------------ */
	private final static Map<String, HttpVersion> CACHE = new HashMap<>();
	static {
		for (HttpVersion version : HttpVersion.values()) {
			CACHE.put(version.toString(), version);
		}
	}

	/**
	 * Case insensitive fromString() conversion.
	 * 
	 * @param version the String to convert to enum constant.
	 * @return Found {@link HttpVersion} matched giving <code>version</code>. Or
	 *         not found, {@link #HTTP_1_1} returns.
	 */
	public static HttpVersion fromString(String version) {
		HttpVersion v = CACHE.get(version);
		if (null != v)
			return v;
		return HTTP_1_1;
	}

	/**
	 * Get {@link HttpVersion} from version number.
	 * 
	 * @param version Version number.
	 * @return {@link HttpVersion} matched the giving number.
	 * @throws IllegalArgumentException If not matched found.
	 */
	public static HttpVersion fromVersion(int version) throws IllegalArgumentException {
		switch (version) {
		case 9:
			return HttpVersion.HTTP_0_9;
		case 10:
			return HttpVersion.HTTP_1_0;
		case 11:
			return HttpVersion.HTTP_1_1;
		default:
			throw new IllegalArgumentException();
		}
	}

	/* ------------------------------------------------------------ */
	/**
	 * Optimised lookup to find a Http Version and whitespace in a byte array.
	 * 
	 * @param bytes Array containing ISO-8859-1 characters
	 * @param position The first valid index
	 * @param limit The first non valid index
	 * @return A HttpMethod if a match or {@link #HTTP_1_1} if no easy match.
	 */
	public static HttpVersion lookAheadGet(byte[] bytes, int position, int limit) {
		int length = limit - position;
		if (length < 8)
			return HttpVersion.HTTP_1_1;

		if (bytes[position + 4] == '/' && bytes[position + 6] == '.' && Character.isWhitespace((char) bytes[position + 8])
				&& ((bytes[position] == 'H' && bytes[position + 1] == 'T' && bytes[position + 2] == 'T' && bytes[position + 3] == 'P')
						|| (bytes[position] == 'h' && bytes[position + 1] == 't' && bytes[position + 2] == 't' && bytes[position + 3] == 'p'))) {

			switch (bytes[position + 5]) {
			case '1':

				switch (bytes[position + 7]) {
				case '0':
					return HTTP_1_0;
				case '1':
					return HTTP_1_1;
				default:
					break;
				}
				break;

			case '2':

				switch (bytes[position + 7]) {
				case '0':
					return HTTP_2_0;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}

		return HTTP_1_1;
	}

	/* ------------------------------------------------------------ */
	/**
	 * Optimised lookup to find a HTTP Version and trailing white space in a
	 * byte array.
	 * 
	 * @param buffer buffer containing ISO-8859-1 characters
	 * @return A HttpVersion if a match or null if no easy match.
	 */
	public static HttpVersion lookAheadGet(ByteBuffer buffer) {
		if (buffer.hasArray())
			return lookAheadGet(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.arrayOffset() + buffer.limit());
		return null;
	}

	private final byte[] bytes;

	private final String str;

	private final int version;

	/* ------------------------------------------------------------ */
	HttpVersion(String s, int version) {
		str = s;
		bytes = s.getBytes(HttpHeader.CHARSET);
		this.version = version;
	}

	/**
	 * @return String presentation of this version.
	 */
	public String asString() {
		return str;
	}

	/**
	 * @return Version number of this version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Test whether this {@link HttpVersion} matched the giving string
	 * <code>s</code>.
	 * 
	 * @param s Source string to test.
	 * @return True if matched ignore case.
	 */
	public boolean is(String s) {
		return str.equalsIgnoreCase(s);
	}

	/**
	 * @return Byte array presentation of this version.
	 */
	public byte[] toBytes() {
		return bytes;
	}

	@Override
	public String toString() {
		return str;
	}
}