/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

/**
 * Constants for HTTP protocol.
 * 
 * @author creativor
 */
public final class HttpConstants {

	/**
	 * Token stands for {@literal &}.
	 */
	public static final char AND_TOKEN = '&';
	/**
	 * Close token of HTTP connection.
	 */
	public static final String CONNECTION_CLOSE_TOKEN = "close";
	/**
	 * Connection header name in HTTP message headers.
	 */
	public static final String CONNECTION_HEADER_NAME = "Connection";

	/**
	 * Content length header name in HTTP message headers.
	 */
	public static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";

	/**
	 * Content type header name in HTTP message headers.
	 */
	public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

	/**
	 * Carriage return character.
	 */
	public static final char CR = 13;
	/**
	 * Token stands for '='.
	 */
	public static final char EQUALS_TOKEN = '=';

	/**
	 * Line feed character.
	 */
	public static final char LF = 10;
	/**
	 * Carriage return and line feed string.
	 */
	public static final byte[] CRLF = new byte[] { CR, LF };

	/**
	 * An split character which split message header name and value.
	 */
	public static final char MESSAGE_HEADER_SPLITER = ':';
	/**
	 * The ASCII control characters stands for null character using URL
	 * Encoding.
	 */
	public static final String NUL_URL_ENCODING = "%00";

	/**
	 * Token stands for '%'.
	 */
	public static final char PERCENT_TOKEN = '%';

	/**
	 * Token stands for '+'.
	 */
	public static final char PLUS_TOKEN = '+';
	/**
	 * Token stands for '?'.
	 */
	public static final char QUERY_TOKEN = '?';
	/**
	 * HTTP header name of Rayson request. Identifier for an request send from
	 * Rayson client.
	 */
	public static final String REQUEST_ID_HEADER_NAME = "RID";
	/**
	 * Default request method of Rayson.
	 */
	public static final HttpMethod REQUEST_METHOD = HttpMethod.POST;
	/**
	 * Token stands for '/'.
	 */
	public static final char SLASH_TOKEN = '/';
	/**
	 * Space character.
	 */
	public static final char SPACE = 32;

	/**
	 * Byte array data for status code 200.
	 */
	public static final byte[] STATUS_200_BYTES = new byte[] { '2', '0', '0' };

	/**
	 * Byte array data for status code 400.
	 */
	public static final byte[] STATUS_400_BYTES = new byte[] { '4', '0', '0' };
	/**
	 * Byte array data for status code 404.
	 */
	public static final byte[] STATUS_404_BYTES = new byte[] { '4', '0', '4' };

	private HttpConstants() {
		// Forbidden.
	}
}