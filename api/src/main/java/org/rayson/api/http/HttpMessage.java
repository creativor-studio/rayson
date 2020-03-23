/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import org.rayson.api.annotation.NotNull;
import org.rayson.api.serial.BufferWritable;

/**
 * Stands for an message of HTTP protocol.
 * 
 * @author creativor
 */
public interface HttpMessage extends BufferWritable {
	/**
	 * Default character encoding.UTF-8.
	 */
	public static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * Add a new header to this message.
	 * 
	 * @param header The new header to be added.
	 */
	public void addHeader(@NotNull final HttpHeader header);

	/**
	 * Add a new header to this message.
	 * 
	 * @param name Name of the new header.
	 * @param value Value of the new value.
	 */
	public void addHeader(@NotNull final String name, @NotNull final String value);

	/**
	 * Message body of this message.
	 * 
	 * @return Message body byte array. If no any content,an empty array return.
	 */
	public byte[] getBody();

	/**
	 * @return Body content length of this message.
	 */
	public int getBodyLength();

	/**
	 * Get an header by giving name.
	 * 
	 * @param name Header name.
	 * @return The first header found whose name matched the giving name.
	 *         <code>null</code> returns when not found.
	 */
	public HttpHeader getHeader(@NotNull String name);

	/**
	 * @return Headers object of this message.
	 */
	public abstract Collection<HttpHeader> getHeaders();

	/**
	 * Returns all of the headers as an array of <code>HttpHeader</code>
	 * objects.
	 *
	 * @param name the name of the header for which values will be returned
	 * @return All headers matched the giving name.
	 */
	public List<HttpHeader> getHeaders(@NotNull String name);

	/**
	 * @return Start line of this message.
	 */
	public HttpStartLine getStartLine();

	/**
	 * @return False if the headers contains "Connection: close". By default,
	 *         <code>true</code>.
	 */
	public boolean isKeepAlive();

	/**
	 * Set a header of this message.
	 * 
	 * @see #setHeader(String, String) About the detail.
	 * 
	 * @param header The header to be set.
	 */
	public void setHeader(@NotNull final HttpHeader header);

	/**
	 * Replaces the current value of the first header entry whose name matches
	 * the given name with the given value, adding a new header if no existing
	 * header name matches. This method also removes all matching headers after
	 * the first one.
	 * 
	 * @param name Name of the new header.
	 * @param value Value of the new value.
	 */
	public void setHeader(@NotNull final String name, @NotNull final String value);
}