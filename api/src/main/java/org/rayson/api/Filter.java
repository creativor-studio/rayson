/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.api;

/**
 * A filter is an object that performs filtering tasks on either the RPC request
 * to a service, or on the RPC response from a service, or both. <br>
 * There are two kinds of filter:
 * <ul>
 * <li>Filter on java object format request or response.</li>
 * <li>Filter on HTTP protocol request or response.</li>
 * </ul>
 * Both client side and server side, support those two kinds of filters.
 * Typically, same kind filters are chained together using a Filter Chain, it
 * implements the chain-of-responsibility pattern. <br>
 * Typically, a filter hash a method as following:
 * 
 * <pre>
 * <code>
*	public void doFilter(Request request, Response response, FilterChain chain);
 * </code>
 * </pre>
 * 
 * @author Nick Zhang
 */
public interface Filter {

}
