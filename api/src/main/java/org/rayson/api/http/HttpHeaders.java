/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api.http;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.rayson.api.annotation.NotNull;

/**
 * A container for {@link HttpHeader} objects, which represent the MIME headers
 * present in a MIME part of a message.
 * 
 * @author creativor
 *
 */
public interface HttpHeaders {

	/**
	 * Adds a <code>HttpHeader</code> object with the specified name and value
	 * to this <code>HttpHeaders</code> object's list of headers.
	 * <P>
	 * Note that RFC822 headers can contain only US-ASCII characters.
	 *
	 * @param name Name of the new header.
	 * @param value Value of the new value.
	 * @throws IllegalArgumentException if the header is <code>null</code>.
	 */
	void add(@NotNull final String name, @NotNull final String value);

	/**
	 * Add a new header.
	 * 
	 * @see #add(String, String) About the detail.
	 * @param header New added header.
	 */
	void add(@NotNull final HttpHeader header);

	/**
	 * Find a header by giving a name.
	 * 
	 * @param name Header's name.
	 * @return The first header found whose name matched the giving name.
	 *         <code>null</code> returns when not found.
	 * @throws IllegalArgumentException if the name is <code>null</code>.
	 */
	HttpHeader get(String name);

	/**
	 * Returns all the <code>HttpHeader</code>s in this <code>HttpHeaders</code>
	 * object.
	 *
	 * @return <code>HttpHeaders</code> object's collection of
	 *         <code>HttpHeader</code> objects.
	 */
	Collection<HttpHeader> getAll();

	/**
	 * Returns all of the headers as an array of <code>HttpHeader</code>
	 * objects.
	 *
	 * @param name the name of the header for which values will be returned
	 * @return All headers matched the giving name.
	 * @throws IllegalArgumentException if the name is <code>null</code>.
	 */
	List<HttpHeader> getHeaders(String name);

	/**
	 * Returns all the <code>HttpHeader</code> objects whose name matches a name
	 * in the given array of names.
	 *
	 * @param names an array of <code>String</code> objects with the names for
	 *            which to search
	 * @return an <code>Iterator</code> object over the <code>HttpHeader</code>
	 *         objects whose name matches one of the names in the given list
	 */
	Iterator<HttpHeader> getMatchings(String[] names);

	/**
	 * Returns all of the <code>HttpHeader</code> objects whose name does not
	 * match a name in the given array of names.
	 *
	 * @param names an array of <code>String</code> objects with the names for
	 *            which to search
	 * @return an <code>Iterator</code> object over the <code>HttpHeader</code>
	 *         objects whose name does not match one of the names in the given
	 *         list
	 */
	Iterator<HttpHeader> getNonMatchings(String[] names);

	/**
	 * Remove all <code>HttpHeader</code> objects whose name matches the given
	 * name.
	 *
	 * @param name a <code>String</code> with the name of the header for which
	 *            to search
	 */
	void remove(String name);

	/**
	 * Removes all the header entries from this <code>HttpHeaders</code> object.
	 */
	void removeAll();

	/**
	 * @see #set(String, String) About the detail.
	 *
	 * @param header Header to be set.
	 * @throws IllegalArgumentException if the header is <code>null</code>.
	 */
	void set(HttpHeader header);

	/**
	 * Replaces the current value of the first header entry whose name matches
	 * the given name with the given value, adding a new header if no existing
	 * header name matches. This method also removes all matching headers after
	 * the first one.
	 * <P>
	 * Note that RFC822 headers can contain only US-ASCII characters.
	 *
	 * @param name Name of the new header.
	 * @param value Value of the new value.
	 * @throws IllegalArgumentException if the header is <code>null</code>.
	 */
	void set(@NotNull final String name, @NotNull final String value);

}